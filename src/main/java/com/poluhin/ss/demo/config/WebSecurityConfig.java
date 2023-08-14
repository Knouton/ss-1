package com.poluhin.ss.demo.config;

import com.poluhin.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final UserService userService;
	private final JwtRequestFilter jwtRequestFilter;
	private final PasswordConfig passwordConfig;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers(new AntPathRequestMatcher("/h2-console/**"),
						                 new AntPathRequestMatcher("/info/admin")).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/info/secured/**"),
						                 new AntPathRequestMatcher("/resources/**")).authenticated()
						.requestMatchers(new AntPathRequestMatcher("/info/unsecured/**"),
						                 new AntPathRequestMatcher("/auth/**"),
						                 new AntPathRequestMatcher("/registration/**")).permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.logout(Customizer.withDefaults());
		http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		http.authenticationProvider(daoAuthenticationProvider());

		http.addFilterAt(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordConfig.passwordEncoder());

		authenticationProvider.setUserDetailsService(userService);

		return authenticationProvider;
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
