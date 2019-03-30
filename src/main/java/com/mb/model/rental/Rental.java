package com.mb.model.rental;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mb.model.AbstractEntity;
import com.mb.model.price.RentalPrice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
public class Rental extends AbstractEntity {

	@JoinColumn(name = "PRICE_ID")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private RentalPrice price;
	
	public Rental(LocalDate createdDate, LocalDate updatedDate, RentalPrice price) {
		super(createdDate, updatedDate);
		this.price = price;
	}
}
