package com.mb.assembler.resource.rent;

import com.mb.assembler.resource.film.FilmResource;
import com.mb.dto.PriceDto;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.price.RentalPrice;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentResourceAssemblerSupportTest {

    @Autowired
    private RentResourceAssemblerSupport rentResourceAssemblerSupport;

    @Test
    public void toResource() {
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

        final RentResource result = rentResourceAssemblerSupport.toResource(rental);
        Assert.assertEquals(rental.getId(), result.getRentId());
        Assert.assertEquals(RentalStatus.RENTED, result.getStatus());
        Assert.assertNotNull(result.getFilms());
        Assert.assertFalse(result.getFilms().isEmpty());
        Assert.assertEquals(1, result.getFilms().size());

        final FilmResource resultFilm = result.getFilms().get(0);
        Assert.assertEquals(film.getId(), resultFilm.getFilmId());
        Assert.assertEquals(film.getTitle(), resultFilm.getTitle());
        Assert.assertEquals(film.getType(), resultFilm.getType());
        Assert.assertEquals(film.getYear(), resultFilm.getYear());

        final PriceDto resultPrice = result.getPrice();
        Assert.assertNotNull(resultPrice);
        Assert.assertEquals(actualPrice.getValue(), resultPrice.getValue());
        Assert.assertEquals(actualPrice.getCurrencySymbol(), resultPrice.getCurrency());
    }
}
