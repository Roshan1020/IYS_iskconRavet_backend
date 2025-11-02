package com.iskconRavet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iskconRavet.entities.YatraEventEntity;

public interface YatraEventRepository extends JpaRepository<YatraEventEntity, String> {
	Optional<YatraEventEntity> findById(String id);

	YatraEventEntity findByEventStatus(String eventStatus);
}