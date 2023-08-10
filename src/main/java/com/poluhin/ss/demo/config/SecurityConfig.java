package com.poluhin.ss.demo.config;

import com.poluhin.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserService userService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.headers(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers(new AntPathRequestMatcher("/h2-console/**"),
						                 new AntPathRequestMatcher("/info/admin")).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/info/secured/**"),
						                 new AntPathRequestMatcher("/resource/**")).authenticated()
						.requestMatchers(new AntPathRequestMatcher("/info/unsecured/**")).permitAll()
						.anyRequest().authenticated())
						.httpBasic(Customizer.withDefaults())
						.formLogin(Customizer.withDefaults())
						.logout(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		authenticationProvider.setUserDetailsService(userService);


		return authenticationProvider;
	}
}
