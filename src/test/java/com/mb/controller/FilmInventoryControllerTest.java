package com.mb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mb.assembler.resource.film.FilmResource;
import com.mb.assembler.resource.film.FilmResourceAssemblerSupport;
import com.mb.dto.SearchFilmDto;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.service.film.FilmService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmInventoryControllerTest extends AbstractMockMvcTest {

    @MockBean
    private FilmService filmService;

    @Autowired
    private FilmResourceAssemblerSupport filmResourceAssembler;

    @Autowired
    private PagedResourcesAssembler<Film> pagedAssembler;

    @Test
    public void findOne_filmDoesNotExist() throws Exception {
        final String filmId = "not-existing-film-id";

        when(filmService.findOne(filmId)).thenReturn(Optional.empty());

        mvc.perform(
                get("/films/{id}", filmId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOne() throws Exception {
        final String filmId = "film-id";

        final FilmResource filmResource = new FilmResource();
        filmResource.setFilmId(filmId);
        filmResource.setTitle("title");
        filmResource.setType(FilmType.NEW);
        filmResource.setYear(2021);

        when(filmService.findOne(filmId)).thenReturn(Optional.of(filmResource));

        final MvcResult mvcResult = mvc.perform(
                get("/films/{id}", filmId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final FilmResource result = parseResponse(mvcResult, new TypeReference<FilmResource>() {
        });
        Assert.assertNotNull(result);
        Assert.assertEquals(filmResource, result);
    }

    @Test
    public void findAll_byYear() throws Exception {
        final Integer year = 1990;
        final SearchFilmDto searchFilmDto = SearchFilmDto.builder()
                .year(year)
                .build();

        final Page<Film> films = Page.empty();
        final PagedResources<FilmResource> filmResources = pagedAssembler.toResource(films, filmResourceAssembler);
        when(filmService.findAll(eq(searchFilmDto), any(Pageable.class))).thenReturn(filmResources);

        final MvcResult mvcResult = mvc.perform(
                get("/films")
                        .param("year", year.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final PagedResources<FilmResource> result = parseResponse(mvcResult, new TypeReference<PagedResources<FilmResource>>() {
        });
        Assert.assertEquals(0, result.getContent().size());
    }
}
