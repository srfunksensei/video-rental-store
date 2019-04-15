package com.mb.model.rental;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mb.model.AbstractEntity;
import com.mb.model.film.Film;
import com.mb.model.price.RentalPrice;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rental")
@Getter
@NoArgsConstructor
public class Rental extends AbstractEntity {

	@JoinColumn(name = "PRICE_ID")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private RentalPrice price;
	
	@OneToMany(mappedBy = "rental")
    private Set<RentalFilm> films = new HashSet<>();
	
	public Set<Film> getFilms() {
		return films.stream().map(RentalFilm::getFilm).collect(Collectors.toSet());
	}
	
	public Rental(LocalDate createdDate, LocalDate updatedDate, RentalPrice price) {
		super(createdDate, updatedDate);
		this.price = price;
	}
	
	public Set<RentalFilm> addFilm(final RentalFilm film) {
		films.add(film);
		return films;
	}
}
