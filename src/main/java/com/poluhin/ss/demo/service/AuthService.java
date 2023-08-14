package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.UserEntity;
import com.poluhin.ss.demo.domain.model.RegistrationUser;
import com.poluhin.ss.demo.domain.model.User;
import com.poluhin.ss.demo.domain.model.jwt.JwtRequest;
import com.poluhin.ss.demo.domain.model.jwt.JwtResponse;
import com.poluhin.ss.demo.exceptions.AuthError;
import com.poluhin.ss.demo.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserService userService;
	private final JwtTokenUtils jwtTokenUtils;
	private final AuthenticationManager authenticationManager;

	public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(new AuthError(HttpStatus.UNAUTHORIZED.value(), "Wrong login or password"), HttpStatus.UNAUTHORIZED);
		}
		UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
		String token = jwtTokenUtils.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	public ResponseEntity<?> createNewUser(@RequestBody RegistrationUser registrationUser) {
		if (!registrationUser.getPassword().equals(registrationUser.getConfirmPassword())) {
			return new ResponseEntity<>(new AuthError(HttpStatus.BAD_REQUEST.value(), "password does not match"), HttpStatus.BAD_REQUEST);
		}
		if (userService.findByUsername(registrationUser.getUsername()).isPresent()) {
			return new ResponseEntity<>(new AuthError(HttpStatus.BAD_REQUEST.value(), "UserAlreadyExits"), HttpStatus.BAD_REQUEST);
		}
		UserEntity user = userService.createNewUser(registrationUser);
		return ResponseEntity.ok(new User(user.getId(), user.getName()));
	}
}
