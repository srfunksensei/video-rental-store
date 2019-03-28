package com.mb.assembler;

import java.math.BigDecimal;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mb.assembler.resource.FilmResource;
import com.mb.controller.FilmInventoryController;
import com.mb.model.Film;

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
		resource.setCurrency("SEK");
		resource.setPrice(new BigDecimal(0));
		return resource;
	}

}
