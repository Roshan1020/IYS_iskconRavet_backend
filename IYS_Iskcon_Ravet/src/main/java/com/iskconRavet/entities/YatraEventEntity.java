package com.iskconRavet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_info")
public class YatraEventEntity {

	@Id
	@Column(name = "event_id", unique = true)
	private String eventId; // Keep this as business identifier

	@Column(name = "title", length = 200)
	private String title;

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "event_status")
	private String eventStatus;

	@Column(name = "event_description")
	private String description;

	@Column(name = "upi_ID")
	private String upiID;

	@Column(name = "event_departureDate")
	private String departureDate;

	@Column(name = "event_duration")
	private String duration;

	@ElementCollection
	@Column(name = "installment_amount")
	private List<String> installmentAmount;
	
	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

}
