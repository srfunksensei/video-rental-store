package com.mb.assembler.resource;

import java.util.stream.Collectors;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mb.assembler.FilmResourceAssemblerSupport;
import com.mb.controller.RentController;
import com.mb.dto.PriceDto;
import com.mb.model.price.Price;
import com.mb.model.rental.Rental;

@Component
public class RentResourceAssemblerSupport extends ResourceAssemblerSupport<Rental, RentResource> {
	
	private final FilmResourceAssemblerSupport filmResourceAssembler;

	public RentResourceAssemblerSupport(final FilmResourceAssemblerSupport filmResourceAssembler) {
		super(RentController.class, RentResource.class);
		this.filmResourceAssembler = filmResourceAssembler;
	}
	
	@Override
	public RentResource toResource(Rental entity) {
		RentResource resource = createResourceWithId(entity.getId(), entity);
		resource.setRentId(entity.getId());
		resource.setFilms(entity.getFilms().stream().map(filmResourceAssembler::toResource).collect(Collectors.toList()));
		resource.setPrice(convertPrice(entity.getPrice()));
		return resource;
	}

	private PriceDto convertPrice(final Price price) {
		return new PriceDto(price.getValue(), price.getCurrencySymbol());
	}
}
