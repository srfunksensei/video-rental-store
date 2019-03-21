package com.mb.model.bonusPoint;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "new_release")
@NoArgsConstructor
public class NewReleaseBonusPoint extends BonusPoint {

	public NewReleaseBonusPoint(LocalDate createdDate, LocalDate updatedDate, Long value) {
		super(createdDate, updatedDate, value);
	}
}
