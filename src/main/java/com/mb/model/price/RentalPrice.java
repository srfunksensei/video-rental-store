package com.mb.model.price;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "rental_price")
@NoArgsConstructor
public class RentalPrice extends Price {

	public RentalPrice(LocalDate createdDate, LocalDate updatedDate, String currencySymbol, BigDecimal value) {
		super(createdDate, updatedDate, currencySymbol, value);
	}
}
