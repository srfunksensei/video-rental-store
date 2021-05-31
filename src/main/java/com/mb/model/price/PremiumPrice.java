package com.mb.model.price;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "premium_price")
@NoArgsConstructor
public class PremiumPrice extends Price {
	
	public PremiumPrice(final String currencySymbol, final BigDecimal value) {
		super(currencySymbol, value);
	}
}
