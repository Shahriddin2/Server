package uz.pdp.simple_l.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.simple_l.dto.book.BookCreateDto;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.repository.AuthUserRepository;
import uz.pdp.simple_l.repository.BookRepository;
import uz.pdp.simple_l.service.book.BookService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BookController {

    private final BookService bookService;
    private final Path rootPath = Path.of("files");
    private final BookRepository bookRepository;
    private final AuthUserRepository authUserRepository;

    public BookController(BookService bookService, BookRepository bookRepository, BookRepository bookRepository1, AuthUserRepository authUserRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository1;
        this.authUserRepository = authUserRepository;
    }

    @GetMapping("/book/add/page/{id}")
    public String bookAddPage(@PathVariable UUID id, HttpSession session) {
        session.setAttribute("category_id", id);
        return "book-add";
    }

    @PostMapping("/book/upload")
    public String bookCreate(@ModelAttribute BookCreateDto dto, RedirectAttributes redirectAttributes,HttpSession session) throws IOException {

        UUID id = (UUID)session.getAttribute("category_id");
        bookService.create(dto,id);
        redirectAttributes.addFlashAttribute("successMessage", "Kitob qo`shildi😀");
        return "redirect:/category/get-list";
    }

    @GetMapping("/images/{imageName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) {
        if (!Files.exists(rootPath)){
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                throw new RuntimeException("File yaratishda xatolik:" + e.getMessage());
            }
        }
        Path imagePath = rootPath.resolve(imageName);  // Kitob rasmini saqlash manzili
        Resource resource = new FileSystemResource(imagePath);
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)  // Rasmni uzatish formatini tanlash (JPEG, PNG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/book/{fileName:.+}")
    public ResponseEntity<Resource> downloadBook(@PathVariable String fileName) throws IOException {
        FileSystemResource fileSystemResource = new FileSystemResource(rootPath.resolve(fileName));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment;fileName = " + fileName)
                .body(fileSystemResource);
    }

    @GetMapping("/book/open/{fileName:.+}")
    public ResponseEntity<Resource> openBookInBrowser(@PathVariable String fileName) {
        Path filePath = rootPath.resolve(fileName);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            // Fayl turini aniqlash
            String contentType = "application/octet-stream";
            try {
                contentType = Files.probeContentType(filePath); // Masalan: application/pdf
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Book> books = bookRepository.findAllBooks();
        long authUsersCount = authUserRepository.countActiveUsers();
        model.addAttribute("books", books);
        model.addAttribute("authUsersCount", authUsersCount);
        return "admin-page";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable UUID id) throws IOException {

        Optional<Book> current = bookRepository.findById(id);

        if(current.isPresent()) {
            Book book = current.get();

            String coverFileGeneratedName = book.getCoverFileGeneratedName();
            String fileGeneratedName = book.getFileGeneratedName();
            Path coverFilePath = rootPath.resolve(coverFileGeneratedName);
            Path filePath = rootPath.resolve(fileGeneratedName);

            Files.deleteIfExists(coverFilePath);
            Files.deleteIfExists(filePath);
        }

        bookRepository.deleteById(id);
        return "redirect:/book/get-list";
    }

}
