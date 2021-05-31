package com.mb.model.price;

import com.mb.model.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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
	
	public Price(final String currencySymbol, final BigDecimal value) {
		this.currencySymbol = currencySymbol;
		this.value = value;
	}
}
