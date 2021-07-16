package com.mb.service.film;

import com.mb.assembler.resource.film.FilmModel;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.repository.film.FilmRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @MockBean
    public FilmRepository filmRepository;

    @Autowired
    public FilmService filmService;

    @Test
    public void findOne() {
        final Film dummyFilm = buildDummyFilm();
        when(filmRepository.findById(anyString())).thenReturn(Optional.of(dummyFilm));

        final Optional<FilmModel> resultOpt = filmService.findOne("id");
        Assertions.assertTrue(resultOpt.isPresent());

        final FilmModel filmModel = resultOpt.get();
        Assertions.assertEquals(dummyFilm.getId(), filmModel.getFilmId());
        Assertions.assertEquals(dummyFilm.getTitle(), filmModel.getTitle());
        Assertions.assertEquals(dummyFilm.getType(), filmModel.getType());
        Assertions.assertEquals(dummyFilm.getYear(), filmModel.getYear());
    }

    @Test
    public void findOne_notFound() {
        when(filmRepository.findById(anyString())).thenReturn(Optional.empty());

        final Optional<FilmModel> resultOpt = filmService.findOne("id");
        Assertions.assertFalse(resultOpt.isPresent());
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
