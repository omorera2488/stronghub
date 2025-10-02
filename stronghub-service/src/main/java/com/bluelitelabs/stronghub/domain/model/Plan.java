package com.bluelitelabs.stronghub.domain.model;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "plans", uniqueConstraints = {
		@UniqueConstraint(name = "uq_plan_gym_name", columnNames = { "gym_id", "name" }),
		@UniqueConstraint(name = "uq_plan_gym_id", columnNames = { "gym_id", "id" }) }, indexes = {
				@Index(name = "ix_plans_gym_id_id", columnList = "gym_id,id"),
				@Index(name = "ix_plan_gym", columnList = "gym_id") })
@SQLDelete(sql = "UPDATE plans SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Plan extends BaseTenantScoped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 80)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price", nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(name = "duration_days", nullable = false)
	private Integer durationDays;

	public Plan() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
	}

}
