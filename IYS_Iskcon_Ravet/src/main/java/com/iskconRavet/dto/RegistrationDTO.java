package com.iskconRavet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDTO {
	private String name;
	private String dob;
	private String gender;
	private String center;
	private String maritalStatus;
	private String harinamInitiated;
	private String aadhaarNumber;
	private String address;
	private String mobile;
	private String mentorName;
}