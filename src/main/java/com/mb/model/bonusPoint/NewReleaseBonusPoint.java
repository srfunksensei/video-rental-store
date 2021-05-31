package com.mb.model.bonusPoint;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "new_release")
@NoArgsConstructor
public class NewReleaseBonusPoint extends BonusPoint {

	public NewReleaseBonusPoint(final Long value) {
		super(value);
	}
}
