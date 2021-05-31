package com.mb.repository.rent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.rental.RentalFilm;

@Repository
public interface RentalFilmRepository extends CrudRepository<RentalFilm, String> {
}
