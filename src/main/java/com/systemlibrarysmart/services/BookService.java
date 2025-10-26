package com.systemlibrarysmart.services;

import com.systemlibrarysmart.domain.models.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {
    Book createBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(UUID id);
    List<Book> searchBooksByName(String name);
    List<Book> getBooksByAuthor(String author);
    boolean rentBook(UUID bookId, UUID userId);
    boolean returnBook(UUID bookId, UUID userId);
    List<Book> getAvailableBooks();
    Book updateBook(UUID id, Book book);
    Book updateAvailableCount(UUID id, int count);
    void deleteBook(UUID id);
}