package com.mb.repository.film.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilmWithTypeEqual implements Specification<Film> {
	
	private static final long serialVersionUID = -8016601207618132990L;
	
	private FilmType type;

	@Override
	public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (type == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get("type"), this.type); // FIXME: use metadata instead of string
	}

}
