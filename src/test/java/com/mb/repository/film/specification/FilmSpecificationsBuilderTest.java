package com.mb.repository.film.specification;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

@SpringBootTest
public class FilmSpecificationsBuilderTest {

    @Test
    public void build_noSpecs() {
        final FilmSpecificationsBuilder builder = new FilmSpecificationsBuilder();
        final Specification<Film> specification = builder.build();
        Assert.assertNull("Expected no specification", specification);
    }

    @Test
    public void build_withSpecs() {
        final FilmSpecificationsBuilder builder = new FilmSpecificationsBuilder();
        final Specification<Film> specification = builder
                .with(FilmSpecificationsBuilder.TITLE_SEARCH_KEY, "title")
                .with(FilmSpecificationsBuilder.TYPE_SEARCH_KEY, FilmType.NEW.toString())
                .build();
        Assert.assertNotNull("Expected specification", specification);
    }

    @Test
    public void build_withNotSupportedKey() {
        final FilmSpecificationsBuilder builder = new FilmSpecificationsBuilder();
        final Specification<Film> specification = builder
                .with("not-supported-key", "title")
                .build();
        Assert.assertNull("Expected no specification", specification);
    }
}
