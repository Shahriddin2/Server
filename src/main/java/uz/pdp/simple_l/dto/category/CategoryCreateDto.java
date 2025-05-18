package uz.pdp.simple_l.dto.category;

import uz.pdp.simple_l.enums.CategoryType;

public record CategoryCreateDto(
        String name,
        CategoryType categoryType
) {
}
