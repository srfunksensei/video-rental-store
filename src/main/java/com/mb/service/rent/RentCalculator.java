package com.mb.service.rent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mb.assembler.resource.film.FilmResourceAssemblerSupport;
import com.mb.assembler.resource.rent.RentResource;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalFilm;
import com.mb.model.rental.RentalStatus;
import com.mb.service.rent.calculation.FilmCalculationStrategy;
import com.mb.service.rent.calculation.NewReleaseFilmPriceCalculator;
import com.mb.service.rent.calculation.OldFilmPriceCalculator;
import com.mb.service.rent.calculation.RegularFilmPriceCalculator;

import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

@Component
@AllArgsConstructor
public class RentCalculator {

	private final FilmResourceAssemblerSupport filmResourceAssembler;

	private final OldFilmPriceCalculator oldFilmPriceCalculator;
	private final RegularFilmPriceCalculator regularFilmPriceCalculator;
	private final NewReleaseFilmPriceCalculator newReleaseFilmPriceCalculator;

	public RentResource calculate(final Set<CheckInItemDto> rentItems, final Set<Film> films) {
		if ((rentItems == null || rentItems.isEmpty()) || (films == null || films.isEmpty())) {
			final RentResource rentResource = new RentResource();
			rentResource.setPrice(new PriceDto(BigDecimal.ZERO, "-"));
			return rentResource;
		}

		final Optional<PriceDto> rentPriceOpt = getPrice(films, rentItems);
		Assert.isTrue(rentPriceOpt.isPresent(), "Price should be present");

		return createRentResource(films, rentPriceOpt.get());
	}

	private Optional<PriceDto> getPrice(final Set<Film> films, final Set<CheckInItemDto> rentItems) {
		final List<PriceDto> prices = collectPrices(films, rentItems);
		return prices.stream().reduce((x, y) -> new PriceDto(x.getValue().add(y.getValue()), x.getCurrency()));
	}

	private List<PriceDto> collectPrices(final Set<Film> films, final Set<CheckInItemDto> rentItems) {
		final List<PriceDto> prices = new ArrayList<>();

		prices.addAll(calculatePriceWithCalculatorByType(oldFilmPriceCalculator, films, rentItems));
		prices.addAll(calculatePriceWithCalculatorByType(regularFilmPriceCalculator, films, rentItems));
		prices.addAll(calculatePriceWithCalculatorByType(newReleaseFilmPriceCalculator, films, rentItems));

		return prices;
	}

	private List<PriceDto> calculatePriceWithCalculatorByType(final FilmCalculationStrategy strategy, final Set<Film> films, final Set<CheckInItemDto> rentItems) {
		final List<Long> daysForRent = getDaysForRent(films, strategy.getFilmType(), rentItems);
		return calculateWithCalculator(strategy, daysForRent);
	}

	private List<Long> getDaysForRent(final Set<Film> films, final FilmType type, final Set<CheckInItemDto> rentItems) {
		final Set<Film> filmsByType = filterFilmsByType(films, type);
		final Set<String> filmIds = filmsByType.stream().map(Film::getId).collect(Collectors.toSet());

		return getDaysForRent(rentItems, filmIds);
	}

	private List<Long> getDaysForRent(final Set<CheckInItemDto> rentItems, final Set<String> filmIds) {
		return rentItems.stream().filter(r -> filmIds.contains(r.getFilmId())) //
				.map(CheckInItemDto::getNumOfDays) //
				.collect(Collectors.toList());
	}

	private Set<Film> filterFilmsByType(final Set<Film> films, final FilmType type) {
		return films.stream().filter(f -> f.getType().equals(type)).collect(Collectors.toSet());
	}

	private List<PriceDto> calculateWithCalculator(final FilmCalculationStrategy strategy, final List<Long> daysForRent) {
		return daysForRent.stream().map(strategy::calculatePrice).collect(Collectors.toList());
	}

	private RentResource createRentResource(final Set<Film> films, final PriceDto price) {
		final RentResource resource = new RentResource();

		resource.setPrice(price);
		resource.setFilms(films.stream().map(filmResourceAssembler::toResource).collect(Collectors.toList()));

		return resource;
	}

	public Optional<PriceDto> calculateCheckOutTotal(final Rental rental, final Set<String> filmIds) {
		if (rental == null || filmIds == null || filmIds.isEmpty()) {
			return Optional.empty();
		}

		final Map<Film, Long> filmsWithActualDaysRented = filterReturnedFilmsWithActualDaysRented(rental.getRentalFilms(), filmIds);
		final Set<Film> films = filmsWithActualDaysRented.keySet();
		final Set<CheckInItemDto> rent = filmsWithActualDaysRented.entrySet().stream() //
				.filter(e -> e.getValue() > 0) //
				.map(e -> new CheckInItemDto(e.getValue(), e.getKey().getId())) //
				.collect(Collectors.toSet());

		return getPrice(films, rent);
	}

	private Map<Film, Long> filterReturnedFilmsWithActualDaysRented(final Set<RentalFilm> rentalFilm, final Set<String> filmIds) {
		final Predicate<RentalFilm> rentedFilmPredicate = rf -> filmIds.contains(rf.getFilm().getId())
				&& RentalStatus.RETURNED.equals(rf.getStatus());

		return rentalFilm.stream() //
				.filter(rentedFilmPredicate) //
				.collect(Collectors.toMap(RentalFilm::getFilm, RentalFilm::getActualRentedDays));
	}
}
