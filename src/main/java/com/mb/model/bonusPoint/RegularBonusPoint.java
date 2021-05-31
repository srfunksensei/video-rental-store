package com.mb.model.bonusPoint;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "regular")
@NoArgsConstructor
public class RegularBonusPoint extends BonusPoint {

	public RegularBonusPoint(final Long value) {
		super(value);
	}
}
