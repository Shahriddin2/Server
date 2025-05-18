package uz.pdp.simple_l.dto.book;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class BookDto implements Serializable {
    String name;
    String description;
    String fileOriginalName;
    String fileGeneratedName;
    String coverFileOriginalName;
    String coverFileGeneratedName;
    long fileSize;
    long coverFileSize;
    String fileContentType;
    String coverFileContentType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
