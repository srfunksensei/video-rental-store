package com.mb.assembler.resource.film;

import com.mb.model.film.FilmType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class FilmModel extends RepresentationModel<FilmModel> {
	private String filmId;
	private String title;
	private int year;
	private FilmType type;
}
