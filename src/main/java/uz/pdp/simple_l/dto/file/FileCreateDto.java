package uz.pdp.simple_l.dto.file;

import java.util.UUID;

public record FileCreateDto(
        UUID bookId,
        String originalName,
        String generatedName,
        String contentType,
        long size,
        byte[] content
) {
}
