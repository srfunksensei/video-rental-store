package com.mb.repository.film.specification;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
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
public class FilmWithTypeEqual implements Specification<Film> {
	
	private static final long serialVersionUID = -8016601207618132990L;
	
	private final FilmType type;

	@Override
	public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (type == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get(Film_.type), this.type);
	}

}
