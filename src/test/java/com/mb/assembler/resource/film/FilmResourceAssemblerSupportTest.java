package com.mb.assembler.resource.film;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilmResourceAssemblerSupportTest {

    @Autowired
    private FilmResourceAssemblerSupport filmResourceAssemblerSupport;

    @Test
    public void toModel() {
        final Film film = new Film();
        film.setId("id");
        film.setTitle("title");
        film.setType(FilmType.NEW);
        film.setYear(2020);

        final FilmModel result = filmResourceAssemblerSupport.toModel(film);
        Assertions.assertEquals(film.getId(), result.getFilmId());
        Assertions.assertEquals(film.getTitle(), result.getTitle());
        Assertions.assertEquals(film.getType(), result.getType());
        Assertions.assertEquals(film.getYear(), result.getYear());
    }
}
