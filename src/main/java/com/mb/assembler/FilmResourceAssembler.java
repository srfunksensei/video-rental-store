package com.mb.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.mb.controller.FilmInventoryController;
import com.mb.model.Film;

@Component
public class FilmResourceAssembler implements ResourceAssembler<Film, Resource<Film>> {

	@Override
	public Resource<Film> toResource(Film film) {
		return new Resource<>(film,
				linkTo(methodOn(FilmInventoryController.class).findOne(film.getId())).withSelfRel());
	}

}
