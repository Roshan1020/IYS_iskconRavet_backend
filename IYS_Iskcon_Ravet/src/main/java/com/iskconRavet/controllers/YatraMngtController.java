package com.iskconRavet.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iskconRavet.dto.PaymentInfoDTO;
import com.iskconRavet.dto.PaymentResponseDTO;
import com.iskconRavet.dto.RegistrationDTO;
import com.iskconRavet.services.JwtService;
import com.iskconRavet.services.YatraEventService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "https://iysiskconravetfrontend.vercel.app/")
@RestController
@RequestMapping("/iys")
public class YatraMngtController {

	@Autowired
	YatraEventService yatraEventService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/events")
	public ResponseEntity<List<Map<String, Object>>> getAllEvents() {
		System.out.println("✅ /api/events invoked");

		List<Map<String, Object>> events = new ArrayList<>();

		Map<String, Object> event1 = new HashMap<>();
		event1.put("id", "event-001");
		event1.put("title", "Festival Of Gratitude - 2025");
		event1.put("startDate", "2025-12-07T00:00:00.000Z");
		event1.put("endDate", "2025-12-07T23:59:59.000Z");

		Map<String, Object> registration1 = new HashMap<>();
		registration1.put("open", true);
		registration1.put("label", "Registration On");
		event1.put("registration", registration1);

		Map<String, Object> event2 = new HashMap<>();
		event2.put("id", "event-002");
		event2.put("title", "Mayapur Yatra 2025");
		event2.put("startDate", "2025-12-10T00:00:00.000Z");
		event2.put("endDate", "2025-12-15T00:00:00.000Z");

		Map<String, Object> registration2 = new HashMap<>();
		registration2.put("open", false);
		registration2.put("label", "Registration Closed");
		event2.put("registration", registration2);

		events.add(event1);
		events.add(event2);

		List<Map<String, Object>> allEvents = yatraEventService.fetchAllEventDetails();

		return ResponseEntity.ok(allEvents);
	}

	@GetMapping("/eventRegDetails")
	public ResponseEntity<Map<String, Object>> getEventRegDetails() {

		Map<String, Object> responseData = yatraEventService.getYatraRegistrationDetails();

		return ResponseEntity.ok(responseData);
	}


	@PostMapping(value = "/registration", consumes = { "multipart/form-data" })
	public ResponseEntity<?> yatraRegistration(HttpServletRequest request, @RequestParam("amount") String amount,
			@RequestParam("paymentId") String paymentId,
			@RequestParam(value = "yatraId", required = false) String yatraId,
			@RequestParam(value = "screenshot", required = false) MultipartFile screenshot) {

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid token");
		}

		String token = authHeader.substring(7);
		String email = jwtService.extractUsername(token);
		System.out.println("registrationDTO :" + email);

		PaymentInfoDTO paymentDTO = new PaymentInfoDTO();
		paymentDTO.setAmount(amount);
		paymentDTO.setPaymentId(paymentId);
		paymentDTO.setYatraId(yatraId);

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Registration submitted successfully!");
		response.put("data", Map.of("amount", amount, "paymentId", paymentId, "screenshotUploaded", screenshot != null,
				"yatraId", yatraId));

//		detailsService.updateUserInfo(registrationDTO, photo, email);
		PaymentResponseDTO paymentRes = yatraEventService.insertYatraRegInfo(paymentDTO, screenshot, email);
		System.out.println("payment details "+paymentRes.toString());

		return ResponseEntity.ok(paymentRes);
	}

}
