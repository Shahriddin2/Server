package uz.pdp.simple_l.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.simple_l.entity.ChildCategory;

import java.util.UUID;

@Repository
public interface ChildCategoryRepository extends JpaRepository<ChildCategory, UUID> {

    @Query("SELECT u from ChildCategory u")
    Page<ChildCategory> findAllChildCategory(Pageable pageable);

    boolean existsByName(String name);
}