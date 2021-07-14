package com.mb.assembler.resource.film;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmResourceAssemblerSupportTest {

    @Autowired
    private FilmResourceAssemblerSupport filmResourceAssemblerSupport;

    @Test
    public void toResource() {
        final Film film = new Film();
        film.setId("id");
        film.setTitle("title");
        film.setType(FilmType.NEW);
        film.setYear(2020);

        final FilmResource result = filmResourceAssemblerSupport.toResource(film);
        Assert.assertEquals(film.getId(), result.getFilmId());
        Assert.assertEquals(film.getTitle(), result.getTitle());
        Assert.assertEquals(film.getType(), result.getType());
        Assert.assertEquals(film.getYear(), result.getYear());
    }
}
