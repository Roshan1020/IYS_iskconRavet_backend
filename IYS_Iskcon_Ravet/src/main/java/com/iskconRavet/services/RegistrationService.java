package com.iskconRavet.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iskconRavet.dto.UserRegistrationDto;
import com.iskconRavet.entities.UserEntity;
import com.iskconRavet.entities.VerificationToken;
import com.iskconRavet.repository.UserRepository;
import com.iskconRavet.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final EmailService emailService; // wrapper around JavaMailSender

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void register(UserRegistrationDto registrationDto, String appURL) {
		// create user
		if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already taken");
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(registrationDto.getEmail());
		userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
//		userEntity.setPassword(new BCryptPasswordEncoder().encode(registrationDto.getPassword()));
		userEntity.setEnabled(false);
		userRepository.save(userEntity);

		// create token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(userEntity);
		verificationToken.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
		verificationTokenRepository.save(verificationToken);

		// send email
		String link = appURL + "auth/verify?token=" + token;
		String subject = "Confirm your email";
		String body = "Click the link to verify your email: " + link;

		// appUrl is something like https://mydomain.com or http://localhost:8080 in
		// dev.

		emailService.sendMail(userEntity.getEmail(), subject, body);
	}

	public String verifyToken(String token) {

		var vtOpt = verificationTokenRepository.findByToken(token);
		if (vtOpt.isEmpty()) {
			return "Invalid token";
		}

		var verificationToken = vtOpt.get();
		if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
			verificationTokenRepository.delete(verificationToken);
			return "Token expired";
		}

		var user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);

		verificationTokenRepository.delete(verificationToken);
		return "Email verified, you can now sign in.";
	}

}
