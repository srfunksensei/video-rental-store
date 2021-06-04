package com.mb.dto;

import com.mb.model.film.FilmType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchFilmDto {
    private String title;
    private Integer year;
    private FilmType type;
}
