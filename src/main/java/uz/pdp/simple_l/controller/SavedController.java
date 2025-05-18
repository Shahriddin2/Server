package uz.pdp.simple_l.controller;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uz.pdp.simple_l.configuration.Session;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Favorite;
import uz.pdp.simple_l.repository.BookRepository;
import uz.pdp.simple_l.repository.FavoriteRepository;
import uz.pdp.simple_l.service.book.FavoriteService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SavedController {

    private final FavoriteService favoriteService;
    private final BookRepository bookRepository;
    private final FavoriteRepository favoriteRepository;
    private final Session session;


    public SavedController(FavoriteService favoriteService, BookRepository bookRepository, FavoriteRepository favoriteRepository, Session session) {
        this.favoriteService = favoriteService;
        this.bookRepository = bookRepository;
        this.favoriteRepository = favoriteRepository;
        this.session = session;
    }

    @GetMapping("/saved/books")
    public String getSavedBookList(Model model) {

        Long userId = session.getCurrentUserId();
        List<Favorite> allUserId = favoriteRepository.findAllUserId(userId);
        List<Book> books = allUserId.stream()
                .map(Favorite::getBook)
                .toList();
        model.addAttribute("books", books);
        return "saved book";
    }

    @PostMapping("/saved/book/{id}")
    public String savedBook(@PathVariable UUID id) {

        Long userId = session.getCurrentUserId();

        Optional<Favorite> currentFavorite = favoriteRepository.findByBookId(id);
        if (currentFavorite.isEmpty()) {
            Optional<Book> byId = bookRepository.findById(id);
            Book book = byId.get();
            favoriteService.create(userId, book);
        }

        return "redirect:/user/page-get";
    }

    @PostMapping("/saved/book/delete/{id}")
    @Transactional
    public String deleteBook(@PathVariable UUID id) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isPresent()) {
            Book book = byId.get();
            favoriteRepository.deleteByBook(book);
        }
        return "redirect:/saved/books";
    }
}
