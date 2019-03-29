package com.mb.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.mb.model.film.Film;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilmWithTitleLike implements Specification<Film> {
	
	private static final long serialVersionUID = 360698723191032259L;
	
	private String title;

	@Override
	public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (title == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.like(root.get("title"), this.title); // FIXME: use metadata instead of string
	}

}
