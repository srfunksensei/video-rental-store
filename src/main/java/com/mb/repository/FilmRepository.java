package com.mb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.Film;

@Repository("filmRepository")
public interface FilmRepository extends JpaRepository<Film, Long> {

	Page<Film> findAll(Pageable pageable);
	Page<Film> findAll(Specification<Film> spec, Pageable pageable);
}
