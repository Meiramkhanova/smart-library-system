package com.systemlibrarysmart.daos.impl;

import com.systemlibrarysmart.daos.BookDao;
import com.systemlibrarysmart.domain.models.Book;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryBookDao implements BookDao {
    private static final List<Book> BOOKS = new ArrayList<>();

    @Override
    public List<Book> findAll() {
        return BOOKS;
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return BOOKS.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(Book book) {
        BOOKS.add(book);
    }

    @Override
    public void update(Book book) {
        Optional<Book> existingBook = BOOKS.stream()
                .filter(b -> b.getId().equals(book.getId()))
                .findFirst();

        if (existingBook.isPresent()) {
            int index = BOOKS.indexOf(existingBook.get());
            BOOKS.set(index, book);
        } else {
            throw new NoSuchElementException("Book with id " + book.getId() + " not found");
        }
    }
    @Override
    public void delete(UUID id) {
        boolean removed = BOOKS.removeIf(b -> b.getId().equals(id));
        if (!removed) {
            throw new NoSuchElementException("Book with id " + id + " not found");
        }
    }

    @Override
    public boolean notExistByIsbn(String isbn) {
        return BOOKS.stream()
                .noneMatch(b -> b.getIsbn().equalsIgnoreCase(isbn));
    }

}
