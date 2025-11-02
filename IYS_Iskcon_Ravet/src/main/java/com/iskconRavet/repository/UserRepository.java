package com.iskconRavet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iskconRavet.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findById(Long userId);
}
