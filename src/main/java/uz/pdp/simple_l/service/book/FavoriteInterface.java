package uz.pdp.simple_l.service.book;

import uz.pdp.simple_l.entity.Book;

public interface FavoriteInterface {
    void create(Long userId, Book book);
}
