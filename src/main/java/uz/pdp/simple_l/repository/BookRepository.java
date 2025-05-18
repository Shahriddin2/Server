package uz.pdp.simple_l.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.simple_l.criteria.BookCriteria;
import uz.pdp.simple_l.criteria.BookSpecs;
import uz.pdp.simple_l.dto.book.BookCreateDto;
import uz.pdp.simple_l.entity.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @Query("SELECT u from Book u")
    List<Book> findAllBooks();

    @Query("SELECT u from Book u")
    Page<Book> findAllBooks(Pageable pageable);

}