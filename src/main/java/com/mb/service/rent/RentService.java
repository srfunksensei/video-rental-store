package com.mb.service.rent;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import com.mb.assembler.resource.RentResource;
import com.mb.assembler.resource.RentResourceAssemblerSupport;
import com.mb.dto.CheckInDto;
import com.mb.dto.PriceDto;
import com.mb.model.film.Film;
import com.mb.model.price.RentalPrice;
import com.mb.model.rental.Rental;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.rent.RentRepository;

import lombok.AllArgsConstructor;

@Service("rentService")
@AllArgsConstructor
public class RentService {
	
	private final RentRepository rentRepository;
	private final FilmRepository filmRepository;
	
	private final RentCalculator rentCalculator;
	
	private final RentResourceAssemblerSupport rentResourceAssembler;
	private final PagedResourcesAssembler<Rental> pagedAssembler;

	public RentResource calculate(final Set<CheckInDto> rent) {
		final Set<Film> films = getFilms(rent);
		return rentCalculator.calculate(rent, films);
	}
	
	private Set<Film> getFilms(final Set<CheckInDto> rent) {
		final Set<Long> filmIds = rent.stream().map(CheckInDto::getFilmId).collect(Collectors.toSet());
		return filmRepository.findByIdIn(filmIds);
	}
	
	public RentResource checkIn(final Set<CheckInDto> rent) {
		final Set<Film> films = getFilms(rent);
		final RentResource rentResource = rentCalculator.calculate(rent, films);
		
		final PriceDto rentPrice = rentResource.getPrice();
		final Rental rental = createRental(rentPrice, films);
	
		rentResource.setRentId(rental.getId());
		
		return rentResource;
	}
	
	private Rental createRental(final PriceDto price, final Set<Film> films) {
		final RentalPrice rentalPrice = new RentalPrice(LocalDate.now(), LocalDate.now(), price.getCurrency(), price.getValue());			
		final Rental rental = new Rental(LocalDate.now(), LocalDate.now(), rentalPrice, films);
		return rentRepository.save(rental);
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
				return pageable.getSortOr(Sort.by(Direction.DESC, "updatedDate")); // FIXME: use metadata instead of string
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
