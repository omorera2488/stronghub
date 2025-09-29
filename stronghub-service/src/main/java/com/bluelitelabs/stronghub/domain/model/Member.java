package com.bluelitelabs.stronghub.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "members", uniqueConstraints = {}, indexes = {
		@Index(name = "ix_members_last_name_active", columnList = "last_name") })
public class Member extends BaseAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "first_name", nullable = false, length = 80)
	private String firstName;
	@Column(name = "last_name", nullable = false, length = 80)
	private String lastName;
	@Column(name = "email", nullable = false, length = 120)
	private String email;
	@Column(name = "phone", length = 40)
	private String phone;
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;
	@Column(name = "gender", length = 20)
	private String gender;

	public Member() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
