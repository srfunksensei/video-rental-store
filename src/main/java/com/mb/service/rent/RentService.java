package com.mb.service.rent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import com.mb.assembler.resource.rent.RentResource;
import com.mb.assembler.resource.rent.RentResourceAssemblerSupport;
import com.mb.dto.CheckInDto;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.price.Price;
import com.mb.model.price.RentalPrice;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalFilm;
import com.mb.repository.customer.CustomerRepository;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.rent.RentRepository;
import com.mb.repository.rent.RentalFilmRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentService {

	private final RentRepository rentRepository;
	private final FilmRepository filmRepository;
	private final RentalFilmRepository rentalFilmRepository;
	private final CustomerRepository customerRepository;

	private final RentCalculator rentCalculator;

	private final RentResourceAssemblerSupport rentResourceAssembler;
	private final PagedResourcesAssembler<Rental> pagedAssembler;

	public RentResource calculate(final CheckInDto rent) {
		final Set<CheckInItemDto> rentItems = rent.getItems();
		
		final Set<Film> films = getFilms(rentItems);
		return rentCalculator.calculate(rentItems, films);
	}

	private Set<Film> getFilms(final Set<CheckInItemDto> rentItems) {
		final Set<Long> filmIds = rentItems.stream().map(CheckInItemDto::getFilmId).collect(Collectors.toSet());
		return filmRepository.findByIdIn(filmIds);
	}

	@Transactional
	public Optional<RentResource> checkIn(final CheckInDto rent) {
		final Optional<Customer> customerOpt = customerRepository.findById(rent.getCustomerId());
		if (!customerOpt.isPresent()) {
			return Optional.empty();
		}
		
		final RentResource rentResource = calculate(rent);
		final PriceDto rentPrice = rentResource.getPrice();

		final Map<Film, Long> filmsWithDaysToRent = getFilmsWithDaysToRent(rent.getItems());
		
		final Rental rental = createRental(customerOpt.get(), rentPrice, filmsWithDaysToRent);

		rentResource.setRentId(rental.getId());
		rentResource.setStatus(rental.getStatus());

		return Optional.of(rentResource);
	}
	
	private Map<Film, Long> getFilmsWithDaysToRent(final Set<CheckInItemDto> rentItems) {
		final Set<Film> films = getFilms(rentItems);
		return rentItems.stream() //
				.collect(Collectors.toMap(//
						r -> films.stream().filter(f -> f.getId() == r.getFilmId()).findFirst().get(), //
						CheckInItemDto::getNumOfDays));
	}

	private Rental createRental(final Customer customer, final PriceDto price, final Map<Film, Long> films) {
		final RentalPrice rentalPrice = new RentalPrice(LocalDate.now(), LocalDate.now(), price.getCurrency(), price.getValue());			
		final Rental rental = new Rental(LocalDate.now(), LocalDate.now(), rentalPrice, customer);
		
		final List<RentalFilm> rentalFilms = new ArrayList<>();
		for(Map.Entry<Film, Long> entry : films.entrySet()) {
			final Film film = entry.getKey();
			final Long numOfDaysToRent = entry.getValue();
			
			customer.addBonusPoints(film.getBonus().getValue());
			
			final RentalFilm rf = new RentalFilm(LocalDate.now(), LocalDate.now(), film, rental, numOfDaysToRent);
			film.addRental(rf);
			rental.addRentalFilm(rf);
			rentalFilms.add(rf);
		}
		
		rentalFilmRepository.saveAll(rentalFilms);
		return rentRepository.save(rental);
	}
	
	@Transactional
	public Optional<PriceDto> checkOut(final Long rentId, final Set<Long> filmIds) {
		Optional<Rental> rentalOpt = rentRepository.findById(rentId);
		if (!rentalOpt.isPresent()) {
			return Optional.empty();
		}
		
		final Rental rental = rentalOpt.get();
		
		if (isReturnedSameDay(rental.getCreatedDate())) {
			rental.updateRentalStatusToReturnedForFilms(filmIds);
			
			final PriceDto priceSubcharged = new PriceDto(new BigDecimal(0L), rental.getActualPrice().getCurrencySymbol());
			return Optional.of(priceSubcharged);
		}
		
		final Optional<PriceDto> priceTotalOpt = rentCalculator.calculateCheckOutTotal(rental, filmIds);
		if (!priceTotalOpt.isPresent()) {
			return Optional.empty();
		}
		
		final PriceDto priceTotal = priceTotalOpt.get();
		rental.setChargedPrice(priceTotal);
		rental.updateRentalStatusToReturnedForFilms(filmIds);
		
		final Price priceActual = rental.getActualPrice();
		final PriceDto priceSubcharged = new PriceDto(priceTotal.getValue().subtract(priceActual.getValue()), priceTotal.getCurrency());
		return Optional.of(priceSubcharged);
	}
	
	private boolean isReturnedSameDay(final LocalDate date) {
		return ChronoUnit.DAYS.between(date, LocalDate.now()) == 0;
	}
	
	public PagedResources<RentResource> findAll(final Pageable pageable) {
		final Pageable defaultPageable = getDefaultPageable(pageable);

		final Page<Rental> rentals = rentRepository.findAll(defaultPageable);
		return pagedAssembler.toResource(rentals, rentResourceAssembler);
	}

	private Pageable getDefaultPageable(final Pageable pageable) {
		return new Pageable() {

			@Override
			public Pageable previousOrFirst() {
				return null;
			}

			@Override
			public Pageable next() {
				return null;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}

			@Override
			public Sort getSort() {
				return pageable.getSortOr(Sort.by(Direction.DESC, "updatedDate")); // FIXME: use metadata instead of
																					// string
			}

			@Override
			public int getPageSize() {
				return pageable.getPageSize();
			}

			@Override
			public int getPageNumber() {
				return pageable.getPageNumber();
			}

			@Override
			public long getOffset() {
				return pageable.getOffset();
			}

			@Override
			public Pageable first() {
				return null;
			}
		};
	}

	public Optional<RentResource> findOne(Long id) {
		return rentRepository.findById(id) //
				.map(rentResourceAssembler::toResource);
	}

	public void deleteOne(final Long id) {
		rentRepository.deleteById(id);
	}
}
