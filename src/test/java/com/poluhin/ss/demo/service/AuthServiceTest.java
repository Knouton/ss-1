package com.poluhin.ss.demo.service;

import static com.poluhin.ss.demo.sample.RegistrationUserSample.registrationUser_sample1;
import static com.poluhin.ss.demo.sample.RegistrationUserSample.registrationUser_sampleErr1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.poluhin.ss.demo.domain.entity.UserEntity;
import com.poluhin.ss.demo.domain.model.User;
import com.poluhin.ss.demo.domain.model.jwt.JwtRequest;
import com.poluhin.ss.demo.domain.model.jwt.JwtResponse;
import com.poluhin.ss.demo.exceptions.AuthError;
import com.poluhin.ss.demo.utils.JwtTokenUtils;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	private static final String TOKEN = "token";
	@Mock
	private UserService userService;
	@Mock
	private JwtTokenUtils jwtTokenUtils;
	@Mock
	private AuthenticationManager authenticationManager;
	@InjectMocks
	private AuthService authService;
	private UserEntity userEntitySample1;
	private UserDetails userDetailsSample1;
	private JwtRequest jwtRequestSample1;

	@BeforeEach
	void init() {

		userEntitySample1 = new UserEntity();
		userEntitySample1.setId(1L);
		userEntitySample1.setName("user");
		userEntitySample1.setPassword("userpass");

		userDetailsSample1 = new org.springframework.security.core.userdetails.User("user",
		                                                                            "userpass",
		                                                                            List.of(new SimpleGrantedAuthority("ROLE_USER")));
		jwtRequestSample1 =
				new JwtRequest("user", "userpass");
	}

	@Test
	void createAuthToken_Success() {

		val responseEntityExpected = ResponseEntity.ok(new JwtResponse(TOKEN));
		when(userService.loadUserByUsername("user")).thenReturn(userDetailsSample1);
		when(jwtTokenUtils.generateToken(userDetailsSample1)).thenReturn(TOKEN);

		val result =  authService.createAuthToken(jwtRequestSample1);

		assertEquals(result, responseEntityExpected);
	}

	@Test
	void createAuthToken_UnSuccess() {

		val responseEntityExpected = new ResponseEntity<>(new AuthError(HttpStatus.UNAUTHORIZED.value(),
		                                                                "Wrong login or password"),
		                                                  HttpStatus.UNAUTHORIZED);
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				jwtRequestSample1.getUsername(),
				jwtRequestSample1.getPassword())))
				.thenThrow(new BadCredentialsException(""));


		val result =  authService.createAuthToken(jwtRequestSample1);

		assertEquals(result.getStatusCode(),responseEntityExpected.getStatusCode());
	}

	@Test
	void createNewUser_Success() {

		val responseEntityExpected = ResponseEntity.ok(new User(
				userEntitySample1.getId(),
				userEntitySample1.getName()));
		when(userService.createNewUser(registrationUser_sample1)).thenReturn(userEntitySample1);

		val result = authService.createNewUser(registrationUser_sample1);

		assertEquals(result, responseEntityExpected);

	}

	@Test
	void createNewUserConfirmPasswordNoMatch_UnSuccess() {

		val responseEntityExpected = new ResponseEntity<>(
				new AuthError(HttpStatus.BAD_REQUEST.value(),
				              "password does not match"),
				HttpStatus.BAD_REQUEST);

		val result = authService.createNewUser(registrationUser_sampleErr1);

		assertEquals(result.getStatusCode(), responseEntityExpected.getStatusCode());
	}

	@Test
	void createNewUserUserAlreadyExits_UnSuccess() {

		when(userService.findByUsername(registrationUser_sample1.getUsername())).thenReturn(Optional.of(userEntitySample1));
		val responseEntityExpected = new ResponseEntity<>(
				new AuthError(HttpStatus.BAD_REQUEST.value(),
				              "UserAlreadyExits"),
				HttpStatus.BAD_REQUEST);

		val result = authService.createNewUser(registrationUser_sample1);

		assertEquals(result.getStatusCode(), responseEntityExpected.getStatusCode());
	}
}