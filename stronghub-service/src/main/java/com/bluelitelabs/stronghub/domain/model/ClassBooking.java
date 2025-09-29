package com.bluelitelabs.stronghub.domain.model;

import java.time.Instant;

import com.bluelitelabs.stronghub.domain.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "class_bookings", uniqueConstraints = @UniqueConstraint(name = "uq_booking_member_session", columnNames = {
		"gym_id", "class_session_id", "member_id" }), indexes = {
				@Index(name = "ix_booking_gym", columnList = "gym_id"),
				@Index(name = "ix_booking_session", columnList = "class_session_id"),
				@Index(name = "ix_booking_member", columnList = "member_id"),
				@Index(name = "ix_booking_status", columnList = "status") })
public class ClassBooking extends BaseTenantScoped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "class_session_id", nullable = false)
	private ClassSession classSession;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private BookingStatus status = BookingStatus.BOOKED;

	@Column(name = "booked_at", nullable = false)
	private Instant bookedAt = Instant.now();

	public ClassBooking() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClassSession getClassSession() {
		return classSession;
	}

	public void setClassSession(ClassSession classSession) {
		this.classSession = classSession;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public Instant getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(Instant bookedAt) {
		this.bookedAt = bookedAt;
	}
}
