package com.iskconRavet.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token; // store hashed or plain (see notes) - we'll store plain for clarity
	private Instant expiresAt;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

}
