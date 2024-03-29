package com.mb.repository.film;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.film.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {

	Page<Film> findAll(final Pageable pageable);
	Page<Film> findAll(final Specification<Film> spec, Pageable pageable);
	
	Set<Film> findByIdIn(final Set<String> filmIds);
}
