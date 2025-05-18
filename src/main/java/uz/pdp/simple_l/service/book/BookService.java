package uz.pdp.simple_l.service.book;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.pdp.simple_l.dto.book.BookCreateDto;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Category;
import uz.pdp.simple_l.repository.BookRepository;
import uz.pdp.simple_l.repository.CategoryRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService implements BookServiceInterface {

    private final BookRepository bookRepository;
    /*private final Path rootPath = Path.of("C:\\Deplom ishi\\Simple_L\\files");*/
    private final Path rootPath = Path.of("files");
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String create(BookCreateDto dto, UUID categoryId) throws IOException {
        Book book = new Book();
        String bookFileOriginalName = dto.bookFile().getOriginalFilename();
        String coverFileOriginalName = dto.coverFile().getOriginalFilename();
        String bookFileGeneratedName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(bookFileOriginalName);
        String coverFileGeneratedName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(coverFileOriginalName);

        Optional<Category> byId = categoryRepository.findById(categoryId);

        if (byId.isPresent()) {
            Category category = byId.get();
            book.setCategory(category);
        }

        book.setName(dto.name());
        book.setDescription(dto.description());
        book.setFileOriginalName(bookFileOriginalName);
        book.setFileGeneratedName(bookFileGeneratedName);
        book.setCoverFileOriginalName(coverFileOriginalName);
        book.setCoverFileGeneratedName(coverFileGeneratedName);
        book.setFileSize(dto.bookFile().getSize());
        book.setCoverFileSize(dto.coverFile().getSize());
        book.setFileContentType(dto.bookFile().getContentType());
        book.setCoverFileContentType(dto.coverFile().getContentType());
        bookRepository.save(book);

        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }
        Files.copy(dto.bookFile().getInputStream(), rootPath.resolve(bookFileGeneratedName), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(dto.coverFile().getInputStream(), rootPath.resolve(coverFileGeneratedName), StandardCopyOption.REPLACE_EXISTING);

        return bookFileOriginalName + ":" + coverFileOriginalName;
    }
}
