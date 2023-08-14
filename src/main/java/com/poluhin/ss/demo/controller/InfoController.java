package com.poluhin.ss.demo.controller;

import static org.springframework.http.ResponseEntity.ok;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

	@GetMapping("/secured")
	public ResponseEntity<String> securedData() {
		return ok("secured data");
	}

	@GetMapping("/unsecured")
	public ResponseEntity<String> unsecuredData() {
		return ok("unsecured data");
	}

	@GetMapping("/admin")
	public ResponseEntity<String> adminData() {
		return ok("admin data");
	}

}
