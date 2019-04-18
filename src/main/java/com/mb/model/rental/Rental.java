package com.mb.model.rental;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mb.dto.PriceDto;
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

	@JoinColumn(name = "ACTUAL_PRICE_ID")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private RentalPrice actualPrice;
	
	@JoinColumn(name = "CHARGED_PRICE_ID")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private RentalPrice chargedPrice;
	
	@OneToMany(mappedBy = "rental")
    private Set<RentalFilm> films = new HashSet<>();
	
	public Set<RentalFilm> getRentalFilms() {
		return films;
	}
	
	public Set<Film> getFilms() {
		return films.stream().map(RentalFilm::getFilm).collect(Collectors.toSet());
	}
	
	public void setChargedPrice(final PriceDto chargedPriceDto) {
		if (chargedPriceDto == null) {
			return;
		}
		
		chargedPrice.setCurrencySymbol(chargedPriceDto.getCurrency());
		chargedPrice.setValue(chargedPriceDto.getValue());
		chargedPrice.setUpdatedDate(LocalDate.now());
		
		setUpdatedDate(LocalDate.now());
	}
	
	public Rental(LocalDate createdDate, LocalDate updatedDate, RentalPrice actualPrice) {
		super(createdDate, updatedDate);
		this.actualPrice = actualPrice;
		this.chargedPrice = new RentalPrice(actualPrice.getCreatedDate(), actualPrice.getUpdatedDate(), actualPrice.getCurrencySymbol(), actualPrice.getValue());
	}
	
	public Set<RentalFilm> addRentalFilm(final RentalFilm film) {
		films.add(film);
		return films;
	}
	
	public RentalStatus getStatus() {
		return isClosed() ? RentalStatus.RETURNED : RentalStatus.RENTED;
	}
	
	private boolean isClosed() {
		final List<RentalStatus> statuses = films.stream().map(RentalFilm::getStatus).collect(Collectors.toList());
		return statuses.stream().allMatch(s -> s.equals(RentalStatus.RETURNED));
	}
}
