package com.mb.model.bonusPoint;

import com.mb.model.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "bonus_point")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@NoArgsConstructor
public class BonusPoint extends AbstractEntity {

	@Column(nullable = false)
	private Long value;
	
	public BonusPoint(final Long value) {
		this.value = value;
	}
}
