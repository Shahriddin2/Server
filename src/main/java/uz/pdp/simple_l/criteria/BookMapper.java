package uz.pdp.simple_l.criteria;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.simple_l.dto.book.BookCreateDto;
import uz.pdp.simple_l.dto.book.BookDto;
import uz.pdp.simple_l.entity.Book;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookMapper extends GenericMapper<Book, BookDto, BookCreateDto> {
}
