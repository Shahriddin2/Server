package uz.pdp.simple_l.search;

import org.springframework.data.jpa.domain.Specification;

public interface GlobalSearch<T, C> {
    Specification<T> getSpecification(C criteria);
}
