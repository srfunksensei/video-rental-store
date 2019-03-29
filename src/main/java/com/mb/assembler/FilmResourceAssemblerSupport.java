package com.mb.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mb.assembler.resource.FilmResource;
import com.mb.controller.FilmInventoryController;
import com.mb.model.film.Film;

@Component
public class FilmResourceAssemblerSupport extends ResourceAssemblerSupport<Film, FilmResource> {

	public FilmResourceAssemblerSupport() {
		super(FilmInventoryController.class, FilmResource.class);
	}

	@Override
	public FilmResource toResource(Film entity) {
		FilmResource resource = createResourceWithId(entity.getId(), entity);
		resource.setTitle(entity.getTitle());
		resource.setYear(entity.getYear());
		resource.setType(entity.getType());
		return resource;
	}

}
