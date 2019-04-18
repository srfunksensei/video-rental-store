package com.mb.model.customer;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.mb.model.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer", indexes = { @Index(columnList = "username", name = "customer_username_idx") })
@Getter
@Setter
@NoArgsConstructor
public class Customer extends AbstractEntity {

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "bonus_points", nullable = false)
	private Long bonusPoints;
	
	public Customer(LocalDate createdDate, LocalDate updatedDate, String firstName, String lastName, String username,
			Long bonusPoints) {
		super(createdDate, updatedDate);
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bonusPoints = bonusPoints;
	}
	
	public Long addBonusPoints(final Long points) {
		bonusPoints += points;
		return bonusPoints;
	}
}