package com.mb.repository;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;

public class GenericQuerySpecs {

    public static <T> Specification<T> all() {
        return (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> andEqual(final Specification<T> specs, final SingularAttribute<T, String> attribute, final String value) {
        if (value == null || value.trim().isEmpty()) {
            return specs;
        }

        return specs.and(equal(attribute, value));
    }

    public static <T> Specification<T> equal(final SingularAttribute<T, String> attribute, final String value) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.get(attribute)), value
        );
    }

    public static <T, E> Specification<T> andEqual(final Specification<T> specs, final SingularAttribute<T, E> attribute, final E value) {
        if (value == null) {
            return specs;
        }

        return specs.and(equal(attribute, value));
    }

    public static <T, E> Specification<T> equal(final SingularAttribute<T, E> attribute, final E value) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(attribute), value
        );
    }
}
