package uz.pdp.simple_l.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.simple_l.criteria.BookCriteria;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.repository.BookRepository;
import uz.pdp.simple_l.search.BookGlobalSearch;

import java.util.List;

@Controller
public class BookSearchController {

    private final BookGlobalSearch bookGlobalSearch;
    private final BookRepository bookRepository;

    public BookSearchController(BookGlobalSearch bookGlobalSearch, BookRepository bookRepository) {
        this.bookGlobalSearch = bookGlobalSearch;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/book/search")
    public String bookSearch(@RequestParam String query, Model model) {
        BookCriteria criteria = new BookCriteria();

        if (query == null || query.isEmpty()) {
            return "redirect:/user/page-get";
        }
        criteria.setQuery(query);

        List<Book> books = bookRepository.findAll(bookGlobalSearch.getSpecification(criteria));
        model.addAttribute("books", books);
        return "tests";
    }
}
