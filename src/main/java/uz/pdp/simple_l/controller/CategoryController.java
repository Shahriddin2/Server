package uz.pdp.simple_l.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.simple_l.dto.category.CategoryCreateDto;
import uz.pdp.simple_l.dto.category.ChildCategoryCreateDto;
import uz.pdp.simple_l.entity.Category;
import uz.pdp.simple_l.entity.ChildCategory;
import uz.pdp.simple_l.repository.CategoryRepository;
import uz.pdp.simple_l.repository.ChildCategoryRepository;
import uz.pdp.simple_l.service.category.CategoryCreateService;
import uz.pdp.simple_l.service.category.ChildCategoryService;

import java.util.Optional;
import java.util.UUID;

@Controller
public class CategoryController {

    private final CategoryCreateService categoryCreateService;
    private final CategoryRepository categoryRepository;
    private final ChildCategoryService childCategoryService;
    private final ChildCategoryRepository childCategoryRepository;

    public CategoryController(CategoryCreateService categoryCreateService, CategoryRepository categoryRepository, ChildCategoryService childCategoryService, ChildCategoryRepository childCategoryRepository) {
        this.categoryCreateService = categoryCreateService;
        this.categoryRepository = categoryRepository;
        this.childCategoryService = childCategoryService;
        this.childCategoryRepository = childCategoryRepository;
    }

    @GetMapping("/category/get/page")
    public String category() {
        return "category-page";
    }

    @PostMapping("/create/category")
    public String createCategory(@ModelAttribute CategoryCreateDto dto,
                                 RedirectAttributes redirectAttributes) {
        if (dto.name() == null || dto.name().isEmpty()
                || dto.categoryType() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Berilgan barcha maydonlarni to`ldiring!");
            return "redirect:/category/get/page";
        }

        boolean currentCategory = categoryRepository.existsByName(dto.name());
        if (currentCategory) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bu kategoriya allaqachon yaratilgan!");
            return "redirect:/category/get/page";
        }
        categoryCreateService.createCategory(dto);
        return "redirect:/control-page";
    }

    @GetMapping("/auth/add/{id}")
    public String addChildCategory(@PathVariable UUID id, HttpSession session) {
        session.setAttribute("parent_category_id", id);
        return "child-category-page";
    }

    @PostMapping("/auth/create/child/category")
    public String createChildCategory(
            @ModelAttribute ChildCategoryCreateDto dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        boolean currentChildCategory = childCategoryRepository.existsByName(dto.name());
        if (currentChildCategory) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bu kategoriya allaqachon yaratilgan!");
            return "redirect:/auth/add/{id}";
        }

        UUID id = (UUID) session.getAttribute("parent_category_id");
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isPresent()) {
            Category category = byId.get();
            ChildCategory childCategory = new ChildCategory();
            childCategory.setCategory(category);
            childCategoryService.create(dto, id);
        }
        session.removeAttribute("parent_category_id");
        return "redirect:/control-page";
    }

    @GetMapping("/category/get-list")
    public String activeUsers(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Category> categoryPage = categoryRepository.findAllCategories(pageable);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "category-list";
    }


    @GetMapping("/category/get-child-category-list")
    public String childCategoryList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<ChildCategory> childCategories = childCategoryRepository.findAllChildCategory(pageable);
        model.addAttribute("childCategories", childCategories);
        model.addAttribute("totalPages", childCategories.getTotalPages());
        model.addAttribute("currentPage", page);
        return "child-category-list";
    }

    @PostMapping("/auth/category/delete/{id}")
    @Transactional
    public String deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
        return "redirect:/control-page";
    }

    @PostMapping("/auth/category/child/delete/{id}")
    public String deleteChildCategory(@PathVariable UUID id) {
        childCategoryRepository.deleteById(id);
        return "redirect:/category/get-list";
    }
}
