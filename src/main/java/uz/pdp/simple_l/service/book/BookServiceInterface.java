package uz.pdp.simple_l.service.book;

import uz.pdp.simple_l.dto.book.BookCreateDto;

import java.io.IOException;
import java.util.UUID;

public interface BookServiceInterface {
    String create(BookCreateDto dto, UUID id) throws IOException;
}
