package uz.pdp.simple_l.dto.book;

import org.springframework.web.multipart.MultipartFile;

public record BookCreateDto(
        String name,
        String description,
        MultipartFile coverFile,
        MultipartFile bookFile
) {
}
