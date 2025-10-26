package com.systemlibrarysmart.daos;

import com.systemlibrarysmart.domain.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookDao {
    List<Book> findAll();
    Optional<Book> findById(UUID id);
    void save(Book book);
    void update(Book book);
    void delete(UUID id);
    boolean notExistByIsbn(String isbn);
}
