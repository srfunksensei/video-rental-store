package com.mb.model.bonusPoint;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "regular")
@NoArgsConstructor
public class RegularBonusPoint extends BonusPoint {

	public RegularBonusPoint(LocalDate createdDate, LocalDate updatedDate, Long value) {
		super(createdDate, updatedDate, value);
	}
}
