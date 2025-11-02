package com.iskconRavet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iskconRavet.entities.PaymentInfoEntity;
import com.iskconRavet.entities.UserEntity;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfoEntity, Long> {
	List<PaymentInfoEntity> findByUserId(Long userId);

	PaymentInfoEntity findByPaymentId(String paymentId);

	boolean existsByYatraIdAndUser(String yatraId, UserEntity user);

}