package com.mb.service.film;

import com.mb.assembler.resource.film.FilmResource;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.repository.film.FilmRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmServiceTest {

    @MockBean
    public FilmRepository filmRepository;

    @Autowired
    public FilmService filmService;

    @Test
    public void findOne() {
        final Film dummyFilm = buildDummyFilm();
        when(filmRepository.findById(anyString())).thenReturn(Optional.of(dummyFilm));

        final Optional<FilmResource> resultOpt = filmService.findOne("id");
        Assert.assertTrue(resultOpt.isPresent());

        final FilmResource filmResource = resultOpt.get();
        Assert.assertEquals(dummyFilm.getId(), filmResource.getFilmId());
        Assert.assertEquals(dummyFilm.getTitle(), filmResource.getTitle());
        Assert.assertEquals(dummyFilm.getType(), filmResource.getType());
        Assert.assertEquals(dummyFilm.getYear(), filmResource.getYear());
    }

    @Test
    public void findOne_notFound() {
        when(filmRepository.findById(anyString())).thenReturn(Optional.empty());

        final Optional<FilmResource> resultOpt = filmService.findOne("id");
        Assert.assertFalse(resultOpt.isPresent());
    }

    private Film buildDummyFilm() {
        final Film film = new Film();
        film.setId("id");
        film.setTitle("title");
        film.setType(FilmType.NEW);
        film.setYear(2020);
        return film;
    }
}
