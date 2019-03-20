package com.mb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.Film;
import com.mb.model.FilmType;

@Repository("filmRepository")
public interface FilmRepository extends JpaRepository<Film, Long> {

	Page<Film> findAll(Pageable pageable);
	Page<Film> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
	Page<Film> findByType(FilmType type, Pageable pageable);
	
}
