package com.poluhin.ss.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUser {
	private String username;
	private String password;
	private String confirmPassword;
}
