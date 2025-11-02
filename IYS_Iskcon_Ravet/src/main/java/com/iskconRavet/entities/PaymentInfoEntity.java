package com.iskconRavet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id; // Primary key for payment table

	@Column(name = "amount")
	private String amount; // can be changed to BigDecimal if you want numeric amount

	@Column(name = "payment_ref_id", unique = true)
	private String paymentId; // e.g. T1234567890

	@Column(name = "yatra_id")
	private String yatraId; // mayapur-2025 etc.

	@Lob
	@Column(name = "paymentReciept", columnDefinition = "LONGBLOB")
	private byte[] screenShot;

	// Each payment belongs to one user
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
