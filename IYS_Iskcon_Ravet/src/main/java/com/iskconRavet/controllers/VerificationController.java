package com.iskconRavet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iskconRavet.services.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class VerificationController {

	@Autowired
	private final RegistrationService registrationService;

	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam String token) {

		String message = registrationService.verifyToken(token);

		return ResponseEntity.ok(message);
	}
}