package com.mb.model.rental;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mb.model.AbstractEntity;
import com.mb.model.film.Film;
import com.mb.model.price.RentalPrice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
public class Rental extends AbstractEntity {

	@JoinColumn(name = "PRICE_ID")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private RentalPrice price;
	
	@ManyToMany
    @JoinTable(
        name = "Rental_Film", 
        joinColumns = { @JoinColumn(name = "rental_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "film_id") }
    )
    private Set<Film> films = new HashSet<>();
	
	public Rental(LocalDate createdDate, LocalDate updatedDate, RentalPrice price, Set<Film> films) {
		super(createdDate, updatedDate);
		this.price = price;
		this.films = films;
	}
}
