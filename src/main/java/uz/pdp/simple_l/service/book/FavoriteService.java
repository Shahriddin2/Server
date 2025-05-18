package uz.pdp.simple_l.service.book;

import org.springframework.stereotype.Service;
import uz.pdp.simple_l.entity.Book;
import uz.pdp.simple_l.entity.Favorite;
import uz.pdp.simple_l.repository.FavoriteRepository;

import java.util.UUID;

@Service
public class FavoriteService implements FavoriteInterface {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }


    @Override
    public void create(Long userId, Book book) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBook(book);
        favoriteRepository.save(favorite);
    }
}
