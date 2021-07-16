package com.mb.service.rent;

import com.mb.assembler.resource.rent.RentModel;
import com.mb.dto.CheckInDto;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.exception.CheckInException;
import com.mb.exception.CheckOutException;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.bonusPoint.BonusPoint;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.price.RentalPrice;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalStatus;
import com.mb.repository.customer.CustomerRepository;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.rent.RentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class RentServiceTest {

    @Autowired
    private RentService rentService;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FilmRepository filmRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
        customerRepository.flush();
        rentRepository.deleteAll();
        rentRepository.flush();
        filmRepository.deleteAll();
        filmRepository.flush();
    }

    @Test
    public void calculate_noItemsReturned() {
        final CheckInDto checkInDto = new CheckInDto("customerId", new HashSet<>());
        final RentModel result = rentService.calculate(checkInDto);
        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getFilms());
        Assertions.assertNull(result.getStatus());
        Assertions.assertNotNull(result.getPrice());
        Assertions.assertNull(result.getRentId());
    }

    @Test
    public void calculate_itemsReturned() {
        final Film film = buildDummyFilm();
        final Film save = filmRepository.save(film);

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, save.getId());

        final Set<CheckInItemDto> items = new HashSet<>();
        items.add(checkInItemDto);

        final CheckInDto checkInDto = new CheckInDto("customerId", items);
        final RentModel result = rentService.calculate(checkInDto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFilms());
        Assertions.assertFalse(result.getFilms().isEmpty());
        Assertions.assertEquals(1, result.getFilms().size());
        Assertions.assertNotNull(result.getPrice());
        Assertions.assertEquals(new BigDecimal(40).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assertions.assertEquals("SEK", result.getPrice().getCurrency());
    }

    @Test
    public void checkIn_customerDoesNotExist() {
        final CheckInDto checkInDto = new CheckInDto("not-existing-customer-id", new HashSet<>());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentService.checkIn(checkInDto));
    }

    @Test
    public void checkIn_noItems() {
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final CheckInDto checkInDto = new CheckInDto(customer.getId(), new HashSet<>());

        Assertions.assertThrows(CheckInException.class, () -> rentService.checkIn(checkInDto));
    }

    @Test
    public void checkIn_withItems() {
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final Film film = buildDummyFilm();
        final Film save = filmRepository.save(film);

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, save.getId());

        final Set<CheckInItemDto> items = new HashSet<>();
        items.add(checkInItemDto);

        final CheckInDto checkInDto = new CheckInDto(customer.getId(), items);

        final RentModel result = rentService.checkIn(checkInDto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getRentId());
        Assertions.assertNotNull(result.getStatus());
        Assertions.assertEquals(RentalStatus.RENTED, result.getStatus());
        Assertions.assertNotNull(result.getFilms());
        Assertions.assertFalse(result.getFilms().isEmpty());
        Assertions.assertEquals(1, result.getFilms().size());
        Assertions.assertNotNull(result.getPrice());
        Assertions.assertEquals(new BigDecimal(40).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assertions.assertEquals("SEK", result.getPrice().getCurrency());

        final List<Rental> rentals = rentRepository.findAll();
        Assertions.assertEquals(1, rentals.size());
    }

    @Test
    public void checkOut_notExistingRentId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentService.checkOut("not-existing-rent-id", new HashSet<>()));
    }

    @Test
    public void checkOut_noFilmsReturned() {
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final RentalPrice rentalPrice = new RentalPrice("RSD", BigDecimal.ONE);
        final Rental rental = new Rental(rentalPrice, customer);
        final Rental saved = rentRepository.save(rental);

        Assertions.assertThrows(CheckOutException.class, () -> rentService.checkOut(saved.getId(), new HashSet<>()));
    }

    @Test
    public void checkOut_returnedSameDay() {
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final Film film = buildDummyFilm();
        final Film save = filmRepository.save(film);

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, save.getId());

        final Set<CheckInItemDto> items = new HashSet<>();
        items.add(checkInItemDto);

        final CheckInDto checkInDto = new CheckInDto(customer.getId(), items);

        final RentModel checkIn = rentService.checkIn(checkInDto);

        final Optional<PriceDto> resultOpt = rentService.checkOut(checkIn.getRentId(), Stream.of(save.getId()).collect(Collectors.toSet()));
        Assertions.assertTrue(resultOpt.isPresent());

        final PriceDto priceDto = resultOpt.get();
        Assertions.assertEquals(BigDecimal.ZERO, priceDto.getValue());
        Assertions.assertEquals("SEK", priceDto.getCurrency());
    }

    @Test
    public void findOne() {
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final RentalPrice rentalPrice = new RentalPrice("RSD", BigDecimal.ONE);
        final Rental rental = new Rental(rentalPrice, customer);
        final Rental saved = rentRepository.save(rental);

        final RentModel result = rentService.findOne(saved.getId());
        Assertions.assertNotNull(result);
    }

    @Test
    public void findOne_notFound() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentService.findOne("not-existing-id"));
    }

    @Test
    public void delete() {
        final RentalPrice rentalPrice = new RentalPrice("RSD", BigDecimal.ONE);
        final Customer customer = customerRepository.save(new Customer("first", "last", "username", 1L));

        final Rental rental = new Rental(rentalPrice, customer);
        final Rental saved = rentRepository.save(rental);

        rentService.deleteOne(saved.getId());
    }

    @Test
    public void deleteOne_notFound() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentService.deleteOne("not-existing-id"));
    }

    private Film buildDummyFilm() {
        final BonusPoint bonusPoint = new BonusPoint(1L);

        final Film film = new Film();
        film.setTitle("title");
        film.setType(FilmType.NEW);
        film.setYear(2020);
        film.setBonus(bonusPoint);
        return film;
    }
}
