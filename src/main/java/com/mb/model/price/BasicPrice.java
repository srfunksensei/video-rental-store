package com.mb.model.price;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "basic_price")
@NoArgsConstructor
public class BasicPrice extends Price {

	public BasicPrice(final String currencySymbol, final BigDecimal value) {
		super(currencySymbol, value);
	}
}
