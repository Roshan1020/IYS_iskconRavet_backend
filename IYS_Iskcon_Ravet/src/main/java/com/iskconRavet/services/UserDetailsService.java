package com.iskconRavet.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iskconRavet.dto.RegistrationDTO;
import com.iskconRavet.entities.UserEntity;
import com.iskconRavet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	private final UserRepository userRepository;

	public void updateUserInfo(RegistrationDTO registrationDTO, MultipartFile photo, String email) {

		UserEntity userEntity = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + email));
		byte[] photoBytes = multiPartFileToByte(photo);

		userDTOToEntity(registrationDTO, userEntity);
		userEntity.setPhoto(photoBytes);
		userRepository.save(userEntity);
	}

	public void userDTOToEntity(RegistrationDTO registrationDTO, UserEntity userEntity) {
		userEntity.setName(registrationDTO.getName());
		userEntity.setDob(registrationDTO.getDob());
		userEntity.setGender(registrationDTO.getGender());
		userEntity.setCenter(registrationDTO.getCenter());
		userEntity.setMaritalStatus(registrationDTO.getMaritalStatus());
		userEntity.setAadhaarNumber(registrationDTO.getAadhaarNumber());
		userEntity.setAddress(registrationDTO.getAddress());
		userEntity.setMobile(registrationDTO.getMobile());
		userEntity.setMentorName(registrationDTO.getMentorName());
		userEntity.setHarinamInitiated(registrationDTO.getHarinamInitiated());

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
