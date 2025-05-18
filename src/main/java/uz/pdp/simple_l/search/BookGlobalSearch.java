package uz.pdp.simple_l.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.pdp.simple_l.criteria.BookCriteria;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Book_;
import uz.pdp.simple_l.util.QueryUtil;

@Component
public class BookGlobalSearch implements GlobalSearch<Book, BookCriteria> {
    @Override
    public Specification<Book> getSpecification(BookCriteria criteria) {
        Specification<Book> specs = Specification.where(null);

        if (criteria.getQuery() != null && !criteria.getQuery().isEmpty()) {
            specs = specs.and(QueryUtil.search(criteria.getQuery(), Book_.name));
        }
        return specs;
    }
}
