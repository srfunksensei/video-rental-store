package com.mb.repository.film.specification;

import com.mb.model.film.Film;
import com.mb.model.film.Film_;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Getter
@AllArgsConstructor
public class FilmWithTitleLike implements Specification<Film> {
	
	private static final long serialVersionUID = 360698723191032259L;
	
	private final String title;

	@Override
	public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (title == null || title.trim().isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.like(root.get(Film_.title), this.title);
	}

}
