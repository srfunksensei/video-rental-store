package com.mb.service.rent;

import com.mb.assembler.resource.rent.RentModel;
import com.mb.assembler.resource.rent.RentResourceAssemblerSupport;
import com.mb.dto.CheckInDto;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.exception.CheckInException;
import com.mb.exception.CheckOutException;
import com.mb.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

	public RentModel calculate(final CheckInDto rent) {
		final Set<CheckInItemDto> rentItems = rent.getItems();
		
		final Set<Film> films = getFilms(rentItems);
		return rentCalculator.calculate(rentItems, films);
	}

	private Set<Film> getFilms(final Set<CheckInItemDto> rentItems) {
		final Set<String> filmIds = rentItems.stream().map(CheckInItemDto::getFilmId).collect(Collectors.toSet());
		return filmRepository.findByIdIn(filmIds);
	}

	@Transactional
	public RentModel checkIn(final CheckInDto rent) {
		final Customer customer = customerRepository.findById(rent.getCustomerId())
				.orElseThrow(ResourceNotFoundException::new);

		if (rent.getItems() == null || rent.getItems().isEmpty()) {
			throw new CheckInException();
		}

		final RentModel rentModel = calculate(rent);
		final PriceDto rentPrice = rentModel.getPrice();

		final Map<Film, Long> filmsWithDaysToRent = getFilmsWithDaysToRent(rent.getItems());
		
		final Rental rental = createRental(customer, rentPrice, filmsWithDaysToRent);

		rentModel.setRentId(rental.getId());
		rentModel.setStatus(rental.getStatus());

		return rentModel;
	}
	
	private Map<Film, Long> getFilmsWithDaysToRent(final Set<CheckInItemDto> rentItems) {
		final Set<Film> films = getFilms(rentItems);
		return rentItems.stream() //
				.collect(Collectors.toMap(//
						r -> films.stream().filter(f -> f.getId().equals(r.getFilmId())).findFirst().get(), //
						CheckInItemDto::getNumOfDays));
	}

	private Rental createRental(final Customer customer, final PriceDto price, final Map<Film, Long> films) {
		final RentalPrice rentalPrice = new RentalPrice(price.getCurrency(), price.getValue());
		final Rental rental = new Rental(rentalPrice, customer);
		
		final List<RentalFilm> rentalFilms = new ArrayList<>();
		for(final Map.Entry<Film, Long> entry : films.entrySet()) {
			final Film film = entry.getKey();
			final Long numOfDaysToRent = entry.getValue();
			
			customer.addBonusPoints(film.getBonus().getValue());
			
			final RentalFilm rf = new RentalFilm(film, rental, numOfDaysToRent);
			film.addRental(rf);
			rental.addRentalFilm(rf);
			rentalFilms.add(rf);
		}
		
		rentalFilmRepository.saveAll(rentalFilms);
		return rentRepository.save(rental);
	}
	
	@Transactional
	public Optional<PriceDto> checkOut(final String rentId, final Set<String> filmIds) {
		final Rental rental = rentRepository.findById(rentId)
				.orElseThrow(ResourceNotFoundException::new);

		if (filmIds == null || filmIds.isEmpty()) {
			throw new CheckOutException();
		}

		if (isReturnedSameDay(rental.getCreatedDate())) {
			rental.updateRentalStatusToReturnedForFilms(filmIds);
			
			final PriceDto priceSubcharged = new PriceDto(BigDecimal.ZERO, rental.getActualPrice().getCurrencySymbol());
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
	
	public PagedModel<RentModel> findAll(final Pageable pageable) {
		final Pageable defaultPageable = getDefaultPageable(pageable);

		final Page<Rental> rentals = rentRepository.findAll(defaultPageable);
		return pagedAssembler.toModel(rentals, rentResourceAssembler);
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
				return pageable.getSortOr(Sort.by(Direction.DESC, "updatedDate"));
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

	@Transactional(readOnly = true)
	public RentModel findOne(final String id) {
		final Rental rental = rentRepository.findById(id)
				.orElseThrow(ResourceNotFoundException::new);

		return rentResourceAssembler.toModel(rental);
	}

	public void deleteOne(final String id) {
		if (!rentRepository.existsById(id)) {
			throw new ResourceNotFoundException();
		}
		rentRepository.deleteById(id);
	}
}
