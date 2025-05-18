package uz.pdp.simple_l.service.category;

import uz.pdp.simple_l.dto.category.ChildCategoryCreateDto;

import java.util.UUID;

public interface ChildCategoryInterface {
    void create(ChildCategoryCreateDto dto, UUID parentCategoryId);
}
