package uz.pdp.simple_l.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.simple_l.configuration.Session;
import uz.pdp.simple_l.entity.AuthUser;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Category;
import uz.pdp.simple_l.entity.ChildCategory;
import uz.pdp.simple_l.repository.AuthUserRepository;
import uz.pdp.simple_l.repository.BookRepository;
import uz.pdp.simple_l.repository.CategoryRepository;
import uz.pdp.simple_l.repository.ChildCategoryRepository;

import java.util.Optional;

@Controller
public class AdminController {

    private final AuthUserRepository authUserRepository;
    private final Session session;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ChildCategoryRepository childCategoryRepository;

    public AdminController(AuthUserRepository authUserRepository, Session session, BookRepository bookRepository, CategoryRepository categoryRepository, ChildCategoryRepository childCategoryRepository) {
        this.authUserRepository = authUserRepository;
        this.session = session;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.childCategoryRepository = childCategoryRepository;
    }


    @GetMapping("/control-page")
    public String usersPage(
            @RequestParam(defaultValue = "1") int userPage,
            @RequestParam(defaultValue = "1") int bookPage,
            @RequestParam(defaultValue = "1") int categoryPage,
            @RequestParam(defaultValue = "1") int childCategoryPage,
            Model model) {
        int pageSize = 5;

        Pageable userPageable = PageRequest.of(userPage - 1, pageSize);
        Pageable bookPageable = PageRequest.of(bookPage - 1, pageSize);
        Pageable categoryPageable = PageRequest.of(categoryPage - 1, pageSize);
        Pageable childCategoryPageable = PageRequest.of(childCategoryPage - 1, pageSize);

        Page<AuthUser> users = authUserRepository.findAllUsers(userPageable);
        model.addAttribute("users", users);
        model.addAttribute("totalUserPages", users.getTotalPages());
        model.addAttribute("currentUserPage", userPage);

        Page<Book> books = bookRepository.findAllBooks(bookPageable);
        model.addAttribute("books", books);
        model.addAttribute("totalBookPages", books.getTotalPages());
        model.addAttribute("currentBookPage", bookPage);

        Page<Category> categories = categoryRepository.findAllCategories(categoryPageable);
        model.addAttribute("categories", categories);
        model.addAttribute("totalCategoryPage", categories.getTotalElements());
        model.addAttribute("currentCategoryPage", categories.getNumber());

        Page<ChildCategory> childCategories = childCategoryRepository.findAllChildCategory(childCategoryPageable);
        model.addAttribute("childCategories", childCategories);
        model.addAttribute("totalChildCategoryPage", childCategories.getTotalElements());
        model.addAttribute("currentChildCategoryPage", childCategories.getNumber());

        String username = session.getCurrentUsername();
        model.addAttribute("username", username);

        long bookCount = bookRepository.count();
        model.addAttribute("bookCount", bookCount);

        long categoryCount = categoryRepository.count();
        model.addAttribute("categoryCount", categoryCount);

        long childCategoryCount = childCategoryRepository.count();
        model.addAttribute("childCategoryCount", childCategoryCount);

        long userCount = authUserRepository.count();
        model.addAttribute("userCount", userCount);

        return "insert/page";
    }

    @PostMapping("/current/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        authUserRepository.deleteById(id);
        return "redirect:/control-page";
    }

    @PostMapping("/current/user/block/{id}")
    public String blockUser(@PathVariable Long id) {
        Optional<AuthUser> currentUser = authUserRepository.findById(id);
        if (currentUser.isPresent()) {
            AuthUser authUser = currentUser.get();
            authUser.setActive(false);
            authUserRepository.save(authUser);
        }
        return "redirect:/control-page";
    }

    @PostMapping("/current/user/activate/{id}")
    public String activeUser(@PathVariable Long id) {
        Optional<AuthUser> currentUser = authUserRepository.findById(id);
        if (currentUser.isPresent()) {
            AuthUser authUser = currentUser.get();
            authUser.setActive(true);
            authUserRepository.save(authUser);
        }
        return "redirect:/control-page";
    }
}
