package com.iskconRavet.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iskconRavet.dto.RegistrationDTO;
import com.iskconRavet.dto.UserRegistrationDto;
import com.iskconRavet.entities.UserEntity;
import com.iskconRavet.repository.UserRepository;
import com.iskconRavet.security.JwtHandler;
import com.iskconRavet.services.JwtService;
import com.iskconRavet.services.RegistrationService;
import com.iskconRavet.services.UserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@CrossOrigin(origins = "https://iysiskconravetfrontend.vercel.app/")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtHandler jwtUtil;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	UserDetailsService detailsService;

	@Autowired
	private JwtService jwtService;
	
    @Value("${app.base-url}")
    private String baseUrl;

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
		Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
		if (userOpt.isEmpty())
			throw new RuntimeException("Invalid email or password");

		UserEntity user = userOpt.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}

		String token = jwtUtil.generateToken(user.getEmail());

		return ResponseEntity.ok(Map.of("token", token, "username", user.getEmail(), "roles", "normalUser", "message",
				"Login successful"));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.ok("Email already exists");
		}

		UserRegistrationDto registrationDto = new UserRegistrationDto();
		registrationDto.setEmail(request.getEmail());
		registrationDto.setPassword(request.getPassword());

		String baseURL = "http://localhost:5173/";
		registrationService.register(registrationDto, baseUrl);
		return ResponseEntity.ok("User registered successfully");
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam String token) {

		String message = registrationService.verifyToken(token);

		return ResponseEntity.ok(message);
	}

	@PostMapping(value = "/userRegister", consumes = { "multipart/form-data" })
	public ResponseEntity<?> registerUser(HttpServletRequest request, @RequestParam("name") String name,
			@RequestParam("dob") String dob, @RequestParam("gender") String gender,
			@RequestParam(value = "center", required = false) String center,
			@RequestParam(value = "maritalStatus", required = false) String maritalStatus,
			@RequestParam(value = "harinamInitiated", required = false) String harinamInitiated,
			@RequestParam(value = "aadhaarNumber", required = false) String aadhaarNumber,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "mentorName", required = false) String mentorName,
			@RequestParam("mobile") String mobile,
			@RequestPart(value = "photo", required = false) MultipartFile photo) {

		RegistrationDTO registrationDTO = new RegistrationDTO(name, dob, gender, center, maritalStatus,
				harinamInitiated, aadhaarNumber, address, mobile, mentorName);

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid token");
		}

		String token = authHeader.substring(7);
		String email = jwtService.extractUsername(token);
		System.out.println("registrationDTO :" + email);
		
		detailsService.updateUserInfo(registrationDTO, photo, email);

		System.out.println("registrationDTO :" + registrationDTO.toString());
		return ResponseEntity.ok(Map.of("message", "Registration saved"));
	}

	@Data
	static class SignupRequest {
		private String name;
		private String email;
		private String password;
	}

	@Data
	static class SigninRequest {
		private String email;
		private String password;
	}

	@Data
	static class SigninResponse {
		private final String email;
		private final String token;
	}
}
