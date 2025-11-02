package com.iskconRavet.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personInfo")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "User_Id")
	private Long id;

	@Column(name = "email_address", unique = true)
	private String email;

	@Column(name = "password_hash")
	private String password;

	@Column(name = "status")
	private boolean enabled;

	@Column(name = "name")
	private String name;

	@Column(name = "DOB")
	private String dob;

	@Column(name = "gender")
	@Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
	private String gender;

	@Column(name = "baceName")
	private String center;

	@Column(name = "maritalStatus")
	private String maritalStatus;

	private String harinamInitiated;

	@Column(name = "aadhaarNumber")
	private String aadhaarNumber;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "mobile", length = 10)
	@Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
	private String mobile;

	@Column(name = "mentorName")
	private String mentorName;

	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PaymentInfoEntity> payments = new ArrayList<>();

}
