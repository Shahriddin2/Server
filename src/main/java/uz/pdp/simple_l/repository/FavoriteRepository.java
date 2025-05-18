package uz.pdp.simple_l.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    @Query("SELECT u from Favorite u where u.userId = :userId")
    List<Favorite> findAllUserId(@PathVariable Long userId);

    @Query("SELECT u from Favorite u where u.book.id = :bookId")
    Optional<Favorite> findByBookId(@PathVariable UUID bookId);

    void deleteByUserId(Long userId);

    @Query("SELECT u from Favorite u where u.book.id = : bookId")
    void deleteByBookId(@PathVariable UUID bookId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.book = :book")
    void deleteByBook(@Param("book") Book book);

}