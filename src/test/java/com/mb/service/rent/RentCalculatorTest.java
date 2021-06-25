package com.mb.service.rent;

import com.mb.assembler.resource.rent.RentResource;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalFilm;
import com.mb.model.rental.RentalStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentCalculatorTest {

    @Autowired
    private RentCalculator rentCalculator;

    @Test
    public void calculate_noValuePassed() {
        RentResource result = rentCalculator.calculate(null, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(BigDecimal.ZERO, result.getPrice().getValue());
        Assert.assertNull(result.getFilms());

        result = rentCalculator.calculate(new HashSet<>(), null);
        Assert.assertNotNull(result);
        Assert.assertEquals(BigDecimal.ZERO, result.getPrice().getValue());
        Assert.assertNull(result.getFilms());

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, UUID.randomUUID().toString());
        final Set<CheckInItemDto> checkInItemDtos = Stream.of(checkInItemDto).collect(Collectors.toSet());

        result = rentCalculator.calculate(checkInItemDtos, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(BigDecimal.ZERO, result.getPrice().getValue());
        Assert.assertNull(result.getFilms());

        result = rentCalculator.calculate(checkInItemDtos, new HashSet<>());
        Assert.assertNotNull(result);
        Assert.assertEquals(BigDecimal.ZERO, result.getPrice().getValue());
        Assert.assertNull(result.getFilms());
    }

    @Test
    public void calculate_oldFilms() {
        final Film film = new Film();
        film.setId(UUID.randomUUID().toString());
        film.setType(FilmType.OLD);
        film.setTitle("old");
        final Set<Film> films = Stream.of(film).collect(Collectors.toSet());

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, film.getId());
        final Set<CheckInItemDto> checkInItemDtos = Stream.of(checkInItemDto).collect(Collectors.toSet());

        final RentResource result = rentCalculator.calculate(checkInItemDtos, films);
        Assert.assertEquals(new BigDecimal(30).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assert.assertNotNull(result.getFilms());
        Assert.assertNull(result.getStatus());
    }

    @Test
    public void calculate_regularFilms() {
        final Film film = new Film();
        film.setId(UUID.randomUUID().toString());
        film.setType(FilmType.REGULAR);
        film.setTitle("regular");
        final Set<Film> films = Stream.of(film).collect(Collectors.toSet());

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, film.getId());
        final Set<CheckInItemDto> checkInItemDtos = Stream.of(checkInItemDto).collect(Collectors.toSet());

        final RentResource result = rentCalculator.calculate(checkInItemDtos, films);
        Assert.assertEquals(new BigDecimal(30).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assert.assertNotNull(result.getFilms());
        Assert.assertNull(result.getStatus());
    }

    @Test
    public void calculate_newReleaseFilms() {
        final Film film = new Film();
        film.setId(UUID.randomUUID().toString());
        film.setType(FilmType.NEW);
        film.setTitle("new");
        final Set<Film> films = Stream.of(film).collect(Collectors.toSet());

        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, film.getId());
        final Set<CheckInItemDto> checkInItemDtos = Stream.of(checkInItemDto).collect(Collectors.toSet());

        final RentResource result = rentCalculator.calculate(checkInItemDtos, films);
        Assert.assertEquals(new BigDecimal(40).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assert.assertNotNull(result.getFilms());
        Assert.assertNull(result.getStatus());
    }

    @Test
    public void calculate_mixFilms() {
        final Film oldFilm = new Film();
        oldFilm.setId(UUID.randomUUID().toString());
        oldFilm.setType(FilmType.OLD);
        oldFilm.setTitle("old");

        final Film regularFilm = new Film();
        regularFilm.setId(UUID.randomUUID().toString());
        regularFilm.setType(FilmType.REGULAR);
        regularFilm.setTitle("regular");

        final Film newFilm = new Film();
        newFilm.setId(UUID.randomUUID().toString());
        newFilm.setType(FilmType.NEW);
        newFilm.setTitle("new");

        final Set<Film> films = Stream.of(oldFilm, regularFilm, newFilm).collect(Collectors.toSet());

        final CheckInItemDto checkInItemOldDto = new CheckInItemDto(1, oldFilm.getId()),
                checkInItemRegularDto = new CheckInItemDto(1, regularFilm.getId()),
                checkInItemNewDto = new CheckInItemDto(1, newFilm.getId());
        final Set<CheckInItemDto> checkInItemDtos = Stream.of(checkInItemOldDto, checkInItemRegularDto, checkInItemNewDto).collect(Collectors.toSet());

        final RentResource result = rentCalculator.calculate(checkInItemDtos, films);
        Assert.assertEquals(new BigDecimal(100).setScale(2, RoundingMode.HALF_UP), result.getPrice().getValue());
        Assert.assertNotNull(result.getFilms());
        Assert.assertNull(result.getStatus());
    }

    @Test
    public void calculateCheckOutTotal_noValuePassed() {
        Optional<PriceDto> result = rentCalculator.calculateCheckOutTotal(null, null);
        Assert.assertFalse(result.isPresent());

        result = rentCalculator.calculateCheckOutTotal(new Rental(), null);
        Assert.assertFalse(result.isPresent());

        result = rentCalculator.calculateCheckOutTotal(new Rental(), new HashSet<>());
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void calculateCheckOutTotal_partiallyReturned() {
        final Film regularFilm = new Film();
        regularFilm.setId(UUID.randomUUID().toString());
        regularFilm.setType(FilmType.REGULAR);
        regularFilm.setTitle("regular");

        final Film newFilm = new Film();
        newFilm.setId(UUID.randomUUID().toString());
        newFilm.setType(FilmType.NEW);
        newFilm.setTitle("new");

        final Set<String> filmIds = Stream.of(regularFilm.getId(), newFilm.getId()).collect(Collectors.toSet());

        final Rental rental = new Rental();

        final RentalFilm regularRentalFilm = new RentalFilm();
        regularRentalFilm.setFilm(regularFilm);
        regularRentalFilm.setNumOfDaysToRent(1L);
        regularRentalFilm.setStatus(RentalStatus.RETURNED);
        regularRentalFilm.setCreatedDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        rental.addRentalFilm(regularRentalFilm);

        final RentalFilm newRentalFilm = new RentalFilm();
        newRentalFilm.setFilm(newFilm);
        newRentalFilm.setNumOfDaysToRent(1L);
        newRentalFilm.setStatus(RentalStatus.RENTED);
        newRentalFilm.setCreatedDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        rental.addRentalFilm(newRentalFilm);

        final Optional<PriceDto> resultOpt = rentCalculator.calculateCheckOutTotal(rental, filmIds);
        Assert.assertTrue(resultOpt.isPresent());

        final PriceDto priceDto = resultOpt.get();
        Assert.assertEquals(new BigDecimal(30).setScale(2, RoundingMode.HALF_UP), priceDto.getValue());
    }

    @Test
    public void calculateCheckOutTotal_fullReturned() {
        final Film regularFilm = new Film();
        regularFilm.setId(UUID.randomUUID().toString());
        regularFilm.setType(FilmType.REGULAR);
        regularFilm.setTitle("regular");

        final Film newFilm = new Film();
        newFilm.setId(UUID.randomUUID().toString());
        newFilm.setType(FilmType.NEW);
        newFilm.setTitle("new");

        final Set<String> filmIds = Stream.of(regularFilm.getId(), newFilm.getId()).collect(Collectors.toSet());

        final Rental rental = new Rental();

        final RentalFilm regularRentalFilm = new RentalFilm();
        regularRentalFilm.setFilm(regularFilm);
        regularRentalFilm.setNumOfDaysToRent(1L);
        regularRentalFilm.setStatus(RentalStatus.RETURNED);
        regularRentalFilm.setCreatedDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        rental.addRentalFilm(regularRentalFilm);

        final RentalFilm newRentalFilm = new RentalFilm();
        newRentalFilm.setFilm(newFilm);
        newRentalFilm.setNumOfDaysToRent(1L);
        newRentalFilm.setStatus(RentalStatus.RETURNED);
        newRentalFilm.setCreatedDate(LocalDate.now().minus(2, ChronoUnit.DAYS));
        rental.addRentalFilm(newRentalFilm);

        final Optional<PriceDto> resultOpt = rentCalculator.calculateCheckOutTotal(rental, filmIds);
        Assert.assertTrue(resultOpt.isPresent());

        final PriceDto priceDto = resultOpt.get();
        Assert.assertEquals(new BigDecimal(110).setScale(2, RoundingMode.HALF_UP), priceDto.getValue());
    }
}
