package com.iskconRavet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
	private String status; // e.g., SUCCESS, ALREADY_EXISTS, FAILED
	private String message; // human-readable message for frontend
}
