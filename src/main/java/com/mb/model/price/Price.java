package com.mb.model.price;

import java.math.BigDecimal;
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
@Table(name = "price")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Price extends AbstractEntity {
	
	@Column(updatable = false, nullable = false)
	private String currencySymbol;
	
	@Column(nullable = false)
	private BigDecimal value;
	
	public Price(LocalDate createdDate, LocalDate updatedDate, String currencySymbol, BigDecimal value) {
		super(createdDate, updatedDate);
		this.currencySymbol = currencySymbol;
		this.value = value;
	}
}
