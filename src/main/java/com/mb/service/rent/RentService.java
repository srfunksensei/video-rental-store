package com.mb.service.rent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mb.assembler.FilmResourceAssemblerSupport;
import com.mb.assembler.resource.RentResource;
import com.mb.dto.CheckInDto;
import com.mb.dto.PriceDto;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.price.RentalPrice;
import com.mb.model.rental.Rental;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.rent.RentRepository;
import com.mb.service.rent.calculation.FilmCalculationStrategy;
import com.mb.service.rent.calculation.NewReleaseFilmPriceCalculator;
import com.mb.service.rent.calculation.OldFilmPriceCalculator;
import com.mb.service.rent.calculation.RegularFilmPriceCalculator;

import lombok.AllArgsConstructor;

@Service("rentService")
@AllArgsConstructor
public class RentService {
	
	private final RentRepository rentRepository;

	private final FilmRepository filmRepository;
	private final FilmResourceAssemblerSupport filmResourceAssembler;

	private final OldFilmPriceCalculator oldFilmPriceCalculator;
	private final RegularFilmPriceCalculator regularFilmPriceCalculator;
	private final NewReleaseFilmPriceCalculator newReleaseFilmPriceCalculator;

	public RentResource calculate(final Set<CheckInDto> rent) {
		final Set<Film> films = getFilms(rent);
		final Optional<PriceDto> rentPrice = getPrice(films, rent);

		return createRentResource(Optional.empty(), films, rentPrice.get());
	}
	
	private Set<Film> getFilms(final Set<CheckInDto> rent) {
		final Set<Long> filmIds = rent.stream().map(CheckInDto::getFilmId).collect(Collectors.toSet());
		return filmRepository.findByIdIn(filmIds);
	}
	
	private Optional<PriceDto> getPrice(final Set<Film> films, final Set<CheckInDto> rent) {
		final List<PriceDto> prices = collectPrices(films, rent);
		return prices.stream()
				.reduce((x, y) -> new PriceDto(x.getValue().add(y.getValue()), x.getCurrency()));
	}

	private List<PriceDto> collectPrices(final Set<Film> films, final Set<CheckInDto> rent) {
		final List<PriceDto> prices = new ArrayList<>();

		prices.addAll(calculatePriceWithCalculatorByType(oldFilmPriceCalculator, films, rent));
		prices.addAll(calculatePriceWithCalculatorByType(regularFilmPriceCalculator, films, rent));
		prices.addAll(calculatePriceWithCalculatorByType(newReleaseFilmPriceCalculator, films, rent));

		return prices;
	}

	private List<PriceDto> calculatePriceWithCalculatorByType(final FilmCalculationStrategy strategy,
			final Set<Film> films, final Set<CheckInDto> rent) {
		final List<Long> daysForRent = getDaysForRent(films, strategy.getFilmType(), rent);
		return calculateWithCalculator(strategy, daysForRent);
	}

	private List<Long> getDaysForRent(final Set<Film> films, FilmType type, final Set<CheckInDto> rent) {
		final Set<Film> filmsByType = filterFilmsByType(films, type);
		final Set<Long> filmIds = filmsByType.stream().map(Film::getId).collect(Collectors.toSet());

		final List<Long> daysForRent = getDaysForRent(rent, filmIds);
		return daysForRent;
	}

	private List<Long> getDaysForRent(final Set<CheckInDto> rent, final Set<Long> filmIds) {
		return rent.stream().filter(r -> filmIds.contains(r.getFilmId())) //
				.map(CheckInDto::getNumOfDays) //
				.collect(Collectors.toList());
	}

	private Set<Film> filterFilmsByType(final Set<Film> films, final FilmType type) {
		return films.stream().filter(f -> f.getType().equals(type)).collect(Collectors.toSet());
	}

	private List<PriceDto> calculateWithCalculator(final FilmCalculationStrategy strategy,
			final List<Long> daysForRent) {
		return daysForRent.stream().map(d -> strategy.calculatePrice(d)).collect(Collectors.toList());
	}

	private RentResource createRentResource(Optional<Rental> rent, Set<Film> films, PriceDto price) {
		RentResource resource = new RentResource();
		if (rent.isPresent()) {
			resource.setRentId(rent.get().getId());
		}
		resource.setPrice(price);
		resource.setFilms(films.stream().map(filmResourceAssembler::toResource).collect(Collectors.toList()));
		return resource;
	}
	
	public Optional<RentResource> checkIn(final Set<CheckInDto> rent) {
		final Set<Film> films = getFilms(rent);
		final Optional<PriceDto> rentPrice = getPrice(films, rent);
		
		final Optional<Rental> optionalRental = createRental(rentPrice);
		if (optionalRental.isPresent()) {
			final Rental rental = rentRepository.save(optionalRental.get());
			final RentResource rentResource = createRentResource(Optional.of(rental), films, rentPrice.get());
			return Optional.of(rentResource);
		}
		
		return Optional.empty();
	}
	
	private Optional<Rental> createRental(final Optional<PriceDto> optionalPrice) {
		if (optionalPrice.isPresent()) {
			final PriceDto price = optionalPrice.get();
			final RentalPrice rentalPrice = new RentalPrice(LocalDate.now(), LocalDate.now(), price.getCurrency(), price.getValue());
			
			final Rental rental = new Rental(LocalDate.now(), LocalDate.now(), rentalPrice);
			return Optional.of(rental);
		}
		
		return Optional.empty();
	}
}
