package com.mb.model.bonusPoint;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "new_release")
public class NewReleaseBonusPoint extends BonusPoint {

}
