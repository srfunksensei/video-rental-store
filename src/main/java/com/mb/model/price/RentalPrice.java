package com.mb.model.price;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "rental_price")
@NoArgsConstructor
public class RentalPrice extends Price {

	public RentalPrice(final String currencySymbol, final BigDecimal value) {
		super(currencySymbol, value);
	}
}
