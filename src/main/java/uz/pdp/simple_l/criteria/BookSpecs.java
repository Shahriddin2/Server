package uz.pdp.simple_l.criteria;


import org.springframework.data.jpa.domain.Specification;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Book_;
import uz.pdp.simple_l.util.QueryUtil;

public final class BookSpecs {

    private BookSpecs() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Book> getSpecification(BookCriteria criteria) {

        Specification<Book> specs = Specification.where(null);

        if (criteria.getName() != null) {
            specs = specs.and(QueryUtil.gte(root -> root.get(Book_.NAME), criteria.getName()));
        }
        if (criteria.getDescription() != null) {
            specs = specs.and(QueryUtil.lte(root -> root.get(Book_.DESCRIPTION), criteria.getDescription()));
        }
        if (criteria.getQuery() != null) {
            specs = specs.and(QueryUtil.search(criteria.getQuery(), Book_.name));
        }
        return specs;
    }
}
