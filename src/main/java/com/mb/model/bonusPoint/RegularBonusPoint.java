package com.mb.model.bonusPoint;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "regular")
public class RegularBonusPoint extends BonusPoint {

}
