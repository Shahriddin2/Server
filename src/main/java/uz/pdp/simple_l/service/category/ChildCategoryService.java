package uz.pdp.simple_l.service.category;

import org.springframework.stereotype.Service;
import uz.pdp.simple_l.dto.category.ChildCategoryCreateDto;
import uz.pdp.simple_l.entity.Category;
import uz.pdp.simple_l.entity.ChildCategory;
import uz.pdp.simple_l.repository.CategoryRepository;
import uz.pdp.simple_l.repository.ChildCategoryRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChildCategoryService implements ChildCategoryInterface {

    private final ChildCategoryRepository childCategoryRepository;
    private final CategoryRepository categoryRepository;

    public ChildCategoryService(ChildCategoryRepository childCategoryRepository, CategoryRepository categoryRepository) {
        this.childCategoryRepository = childCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void create(ChildCategoryCreateDto dto, UUID parentCategoryId) {
        ChildCategory childCategory = new ChildCategory();

        Optional<Category> byId = categoryRepository.findById(parentCategoryId);

        if (byId.isPresent()) {
            Category category = byId.get();
            childCategory.setCategory(category);
        }


        childCategory.setName(dto.name());
        childCategoryRepository.save(childCategory);
    }
}
