package com.poluhin.ss.demo.controller;

import com.poluhin.ss.demo.domain.model.RegistrationUser;
import com.poluhin.ss.demo.domain.model.jwt.JwtRequest;
import com.poluhin.ss.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
		return authService.createAuthToken(authRequest);
	}

	@PostMapping("/registration")
	public ResponseEntity<?> createNewUser(@RequestBody RegistrationUser registrationUserDto) {
		return authService.createNewUser(registrationUserDto);
	}
}
