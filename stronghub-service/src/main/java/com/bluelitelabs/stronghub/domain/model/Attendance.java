package com.bluelitelabs.stronghub.domain.model;

import java.time.Instant;

import com.bluelitelabs.stronghub.domain.enums.AttendanceStatus;

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
@Table(name = "attendances", uniqueConstraints = @UniqueConstraint(name = "uq_att_member_session", columnNames = {
		"gym_id", "class_session_id", "member_id" }), indexes = { @Index(name = "ix_att_gym", columnList = "gym_id"),
				@Index(name = "ix_att_session", columnList = "class_session_id"),
				@Index(name = "ix_att_member", columnList = "member_id") })
public class Attendance extends BaseTenantScoped {

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
	private AttendanceStatus status = AttendanceStatus.PRESENT;

	@Column(name = "attended_at", nullable = false)
	private Instant attendedAt = Instant.now();

	public Attendance() {
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

	public AttendanceStatus getStatus() {
		return status;
	}

	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}

	public Instant getAttendedAt() {
		return attendedAt;
	}

	public void setAttendedAt(Instant attendedAt) {
		this.attendedAt = attendedAt;
	}
}
