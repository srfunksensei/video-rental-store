package com.mb.model.customer;

import com.mb.model.AbstractEntity;
import com.mb.model.rental.Rental;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Rental> rentals = new HashSet<>();
	
	public Customer(final String firstName, final String lastName, final String username, final Long bonusPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bonusPoints = bonusPoints;
	}
	
	public Long addBonusPoints(final Long points) {
		bonusPoints += points;
		return bonusPoints;
	}
	
	public void addRental(final Rental rental) {
		rentals.add(rental);
	}
}