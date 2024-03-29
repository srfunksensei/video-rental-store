package com.mb.repository.film.specification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.mb.model.film.Film;
import com.mb.model.film.FilmType;

public class FilmSpecificationsBuilder {

	public static final String TITLE_SEARCH_KEY = "title";
	public static final String TYPE_SEARCH_KEY = "type";

	private final Map<String, String> params;

	public FilmSpecificationsBuilder() {
		params = new HashMap<>();
	}

	public FilmSpecificationsBuilder with(final String key, final String value) {
		params.put(key, value);
		return this;
	}

	public Specification<Film> build() {
		if (params.size() == 0) {
			return null;
		}

		final Set<Specification<Film>> specs = collectSpecifications();
		if (specs.isEmpty()) {
			return null;
		}

		final Iterator<Specification<Film>> iterator = specs.iterator();

		Specification<Film> result = Specification.where(iterator.next());
		while (iterator.hasNext()) {
			result = Specification.where(result).and(iterator.next());
		}

		return result;
	}

	private Set<Specification<Film>> collectSpecifications() {
		final Set<Specification<Film>> specs = new HashSet<>();

		for (final Map.Entry<String, String> param : params.entrySet()) {
			if (FilmSpecificationsBuilder.TITLE_SEARCH_KEY.equals(param.getKey())) {
				final String title = param.getValue();
				if (title != null && !title.isEmpty()) {
					specs.add(new FilmWithTitleLike(title));
				}
			} else if (FilmSpecificationsBuilder.TYPE_SEARCH_KEY.equals(param.getKey())) {
				final String type = param.getValue();
				if (type != null && !type.isEmpty()) {
					specs.add(new FilmWithTypeEqual(FilmType.valueOf(type)));
				}
			}
		}

		return specs;
	}
}
