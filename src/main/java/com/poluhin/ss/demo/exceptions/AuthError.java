package com.poluhin.ss.demo.exceptions;

import lombok.Data;

import java.util.Date;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AuthError extends RuntimeException{

	private int status;
	private String message;
	private Date timestamp;

	public AuthError(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = new Date();
	}
}
