package uz.pdp.simple_l.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.simple_l.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT u from Category u")
    Page<Category> findAllCategories(Pageable pageable);

    boolean existsByName(String name);

    Optional<Category> findById(UUID id);

    void deleteById(UUID id);
}