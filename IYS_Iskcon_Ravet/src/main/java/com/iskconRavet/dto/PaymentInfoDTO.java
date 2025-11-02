package com.iskconRavet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoDTO {

	private Long id;
	private String amount;
	private String paymentId;
	private String yatraId;
	private String paymentScreenshot;

	private Long userId;
	private String userEmail;
}
