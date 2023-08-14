package com.poluhin.ss.demo.domain.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequest {
	private String username;
	private String password;
}
