package com.mb.model.rental;

import com.mb.dto.PriceDto;
import com.mb.model.AbstractEntity;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.price.RentalPrice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	@OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
	private final Set<RentalFilm> films = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

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

	public Rental(final RentalPrice actualPrice, final Customer customer) {
		this.actualPrice = actualPrice;
		this.chargedPrice = new RentalPrice(actualPrice.getCurrencySymbol(), actualPrice.getValue());
		this.customer = customer;
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

	public void updateRentalStatusToReturnedForFilms(final Set<Long> filmIds) {
		getRentalFilms().stream() //
				.filter(rf -> filmIds.contains(rf.getFilm().getId())) //
				.forEach(rf -> {
					rf.setUpdatedDate(LocalDate.now());
					rf.setStatus(RentalStatus.RETURNED);
				});

		setUpdatedDate(LocalDate.now());
	}
}
