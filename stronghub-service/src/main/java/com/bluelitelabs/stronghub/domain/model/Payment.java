package com.bluelitelabs.stronghub.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.bluelitelabs.stronghub.domain.enums.PaymentMethod;
import com.bluelitelabs.stronghub.domain.enums.PaymentStatus;

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
@Table(name = "payments", uniqueConstraints = {
		@UniqueConstraint(name = "uq_payment_extref", columnNames = { "gym_id", "external_ref" }) }, indexes = {
				@Index(name = "ix_payment_gym", columnList = "gym_id"),
				@Index(name = "ix_payment_member", columnList = "member_id"),
				@Index(name = "ix_payment_subscription", columnList = "subscription_id"),
				@Index(name = "ix_payment_status", columnList = "status") })
public class Payment extends BaseTenantScoped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscription_id")
	private Subscription subscription;

	@Column(name = "amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(name = "currency", nullable = false, length = 10)
	private String currency = "CRC";

	@Enumerated(EnumType.STRING)
	@Column(name = "method", nullable = false, length = 20)
	private PaymentMethod method = PaymentMethod.OTHER;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private PaymentStatus status = PaymentStatus.PAID;

	@Column(name = "paid_at", nullable = false)
	private Instant paidAt = Instant.now();

	@Column(name = "external_ref", length = 120)
	private String externalRef;

	public Payment() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Instant getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Instant paidAt) {
		this.paidAt = paidAt;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}
}
