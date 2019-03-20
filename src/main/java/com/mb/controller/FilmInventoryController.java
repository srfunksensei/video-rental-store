package com.mb.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.FilmResourceAssembler;
import com.mb.model.Film;
import com.mb.repository.FilmRepository;

@RestController
@RequestMapping(value = "/films", produces = "application/hal+json")
public class FilmInventoryController {

	private final FilmRepository filmRepository;

	private final FilmResourceAssembler filmResourceAssembler;

	@Autowired
	public FilmInventoryController(final FilmRepository filmRepository,
			final FilmResourceAssembler filmResourceAssembler) {
		this.filmRepository = filmRepository;
		this.filmResourceAssembler = filmResourceAssembler;
	}

	@GetMapping(value = "/{filmId}")
	public ResponseEntity<Resource<Film>> findOne(@PathVariable Long filmId) {
		return filmRepository.findById(filmId) //
				.map(filmResourceAssembler::toResource) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public Resources<Resource<Film>> findAll(Pageable pageable) {
		final List<Resource<Film>> films = filmRepository.findAll(pageable).stream() //
				.map(filmResourceAssembler::toResource) //
				.collect(Collectors.toList());

		return new Resources<>(films, //
				linkTo(methodOn(FilmInventoryController.class).findAll(pageable)).withSelfRel());
	}
}
