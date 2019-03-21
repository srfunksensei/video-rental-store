package com.mb.model.bonusPoint;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.mb.model.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bonus_point")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
public class BonusPoint extends AbstractEntity {

	@Column(nullable = false)
	private Long value;
	
	public BonusPoint(LocalDate createdDate, LocalDate updatedDate, Long value) {
		super(createdDate, updatedDate);
		this.value = value;
	}
}
