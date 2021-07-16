package com.mb.assembler.resource.rent;

import com.mb.assembler.resource.film.FilmModel;
import com.mb.dto.PriceDto;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.price.RentalPrice;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalFilm;
import com.mb.model.rental.RentalStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class RentResourceAssemblerSupportTest {

    @Autowired
    private RentResourceAssemblerSupport rentResourceAssemblerSupport;

    @Test
    public void toModel() {
        final Film film = new Film();
        film.setId("filmId");
        film.setTitle("title");
        film.setType(FilmType.NEW);
        film.setYear(2020);

        final RentalPrice actualPrice = new RentalPrice();
        actualPrice.setValue(BigDecimal.TEN);
        actualPrice.setCurrencySymbol("SEK");

        final Rental rental = new Rental(actualPrice, new Customer());
        rental.setId("id");

        final RentalFilm rentalFilm = new RentalFilm();
        rentalFilm.setFilm(film);
        rentalFilm.setRental(rental);
        rentalFilm.setStatus(RentalStatus.RENTED);

        rental.addRentalFilm(rentalFilm);

        final RentModel result = rentResourceAssemblerSupport.toModel(rental);
        Assertions.assertEquals(rental.getId(), result.getRentId());
        Assertions.assertEquals(RentalStatus.RENTED, result.getStatus());
        Assertions.assertNotNull(result.getFilms());
        Assertions.assertFalse(result.getFilms().isEmpty());
        Assertions.assertEquals(1, result.getFilms().size());

        final FilmModel resultFilm = result.getFilms().get(0);
        Assertions.assertEquals(film.getId(), resultFilm.getFilmId());
        Assertions.assertEquals(film.getTitle(), resultFilm.getTitle());
        Assertions.assertEquals(film.getType(), resultFilm.getType());
        Assertions.assertEquals(film.getYear(), resultFilm.getYear());

        final PriceDto resultPrice = result.getPrice();
        Assertions.assertNotNull(resultPrice);
        Assertions.assertEquals(actualPrice.getValue(), resultPrice.getValue());
        Assertions.assertEquals(actualPrice.getCurrencySymbol(), resultPrice.getCurrency());
    }
}
