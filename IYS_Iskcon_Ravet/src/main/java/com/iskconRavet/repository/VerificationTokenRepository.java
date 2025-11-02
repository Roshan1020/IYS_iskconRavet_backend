package com.iskconRavet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iskconRavet.entities.UserEntity;
import com.iskconRavet.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	Optional<VerificationToken> findByToken(String token);

	void deleteByUser(UserEntity userEntity);
}
