package com.poluhin.ss.demo.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

	@GetMapping("/secured")
	public String securedData() {
		return "secured data";
	}

	@GetMapping("/unsecured")
	public String unsecuredData() {
		return "unsecured data";
	}

	@GetMapping("/admin")
	public String adminData() {
		return "admin data";
	}

}
