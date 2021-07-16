package com.mb.assembler.resource.rent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mb.assembler.resource.film.FilmModel;
import com.mb.dto.PriceDto;
import com.mb.model.rental.RentalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RentModel extends RepresentationModel<RentModel> {
	@JsonInclude(Include.NON_NULL)
	private String rentId;
	private List<FilmModel> films;
	private PriceDto price;
	@JsonInclude(Include.NON_NULL)
	private RentalStatus status;
}
