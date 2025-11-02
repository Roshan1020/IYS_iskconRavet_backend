package com.iskconRavet.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iskconRavet.dto.PaymentInfoDTO;
import com.iskconRavet.dto.PaymentResponseDTO;
import com.iskconRavet.entities.PaymentInfoEntity;
import com.iskconRavet.entities.UserEntity;
import com.iskconRavet.entities.YatraEventEntity;
import com.iskconRavet.repository.PaymentInfoRepository;
import com.iskconRavet.repository.UserRepository;
import com.iskconRavet.repository.YatraEventRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YatraEventService {

	private final YatraEventRepository yatraEventRepository;
	private final UserRepository userRepository;
	private final PaymentInfoRepository paymentInfoRepository;

	@PostConstruct
	public void init() {
		System.out.println("repo is null? " + (yatraEventRepository == null));
	}

	public List<Map<String, Object>> fetchAllEventDetails() {
		List<Map<String, Object>> events = new ArrayList<>();
		List<YatraEventEntity> yatraEventEntity = yatraEventRepository.findAll();

		for (YatraEventEntity entity : yatraEventEntity) {
			Map<String, Object> event = new HashMap<>();
			event.put("id", entity.getEventId());
			event.put("title", entity.getTitle());
			event.put("startDate", entity.getStartDate());
			event.put("endDate", entity.getEndDate());
			String eventStatus = entity.getEventStatus();

			Map<String, Object> eventstate = new HashMap<>();
			if (eventStatus.equalsIgnoreCase("Active")) {
				eventstate.put("open", true);
				eventstate.put("label", "Registration On");
			} else {
				eventstate.put("open", false);
				eventstate.put("label", "Registration Closed");
			}

			event.put("registration", eventstate);
			events.add(event);
		}

		return events;
	}

	public Map<String, Object> getYatraRegistrationDetails() {

		YatraEventEntity yatraEventEntity = yatraEventRepository.findByEventStatus("Active");

		Map<String, Object> data = new HashMap<>();
		data.put("id", yatraEventEntity.getEventId());
		data.put("title", yatraEventEntity.getTitle());
		data.put("description", yatraEventEntity.getDescription());
		data.put("upiId", yatraEventEntity.getUpiID());
		data.put("notes", "Limited seats available! Please complete your registration early.");
		data.put("departure", yatraEventEntity.getDepartureDate());
		data.put("duration", yatraEventEntity.getDuration());
		data.put("qrUrl", yatraEventEntity.getPhoto());

		Map<String, Object> fees = new HashMap<>();
		fees.put("currencySymbol", "₹");
		fees.put("installments", yatraEventEntity.getInstallmentAmount());
		fees.put("fullAmount", "6000");
		fees.put("description", "Includes travel, stay, and prasadam.");

		data.put("fees", fees);

		return data;

	}

	public PaymentResponseDTO insertYatraRegInfo(PaymentInfoDTO paymentDTO, MultipartFile screenshot, String email) {

		try {
			UserEntity user = userRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found with email: " + email));

			boolean userPaymentExist = isYatraPaymentAlreadyExist(paymentDTO.getYatraId(), user);
			System.out.println("userExist "+userPaymentExist);
			if (userPaymentExist) {
				return new PaymentResponseDTO("ALREADY_EXISTS", "Payment info already uploaded for this Yatra.");
			}

			PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
			paymentInfoEntity.setAmount(paymentDTO.getAmount());
			paymentInfoEntity.setPaymentId(paymentDTO.getPaymentId());
			paymentInfoEntity.setYatraId(paymentDTO.getYatraId());

			byte[] photoBy = multiPartFileToByte(screenshot);
			paymentInfoEntity.setScreenShot(photoBy);

			paymentInfoEntity.setUser(user);

			paymentInfoRepository.save(paymentInfoEntity);

			return new PaymentResponseDTO("SUCCESS", "Payment info uploaded successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return new PaymentResponseDTO("FAILED", "Payment info uploadation failed: " + e.getMessage());
		}

	}

	public boolean isYatraPaymentAlreadyExist(String yatraID, UserEntity userEntity) {

		return paymentInfoRepository.existsByYatraIdAndUser(yatraID, userEntity);
	}

	public byte[] multiPartFileToByte(MultipartFile photo) {
		byte[] photoBytes = null;
		if (photo != null && !photo.isEmpty()) {
			try {
				photoBytes = photo.getBytes(); // Convert to byte array
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return photoBytes;
	}
}
