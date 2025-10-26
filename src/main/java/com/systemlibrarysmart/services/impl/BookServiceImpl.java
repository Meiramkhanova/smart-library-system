package com.systemlibrarysmart.services.impl;

import com.systemlibrarysmart.daos.BookDao;
import com.systemlibrarysmart.daos.UserDao;
import com.systemlibrarysmart.daos.RentDao;
import com.systemlibrarysmart.domain.exceptions.BookAlreadyExistsException;
import com.systemlibrarysmart.domain.exceptions.BookNotFoundException;
import com.systemlibrarysmart.domain.exceptions.RentNotFoundException;
import com.systemlibrarysmart.domain.exceptions.UserNotFoundException;
import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.domain.models.Rent;
import com.systemlibrarysmart.domain.models.User;
import com.systemlibrarysmart.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final UserDao userDao;
    private final RentDao rentDao;

    @Override
    public Book createBook(Book book) {
        log.info("Creating book with name: {}", book.getName());

        return Optional.of(book)
                .filter(b -> bookDao.notExistByIsbn(b.getIsbn()))
                .map(b -> {
                    b.setId(UUID.randomUUID());
                    bookDao.save(b);
                    log.info("Book created successfully with ISBN: {}", b.getIsbn());
                    return b;
                })
                .orElseThrow(() -> {
                    log.error("Book already exists with ISBN: {}", book.getIsbn());
                    return new BookAlreadyExistsException("Book with ISBN " + book.getIsbn() + " already exists");
                });
    }

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookDao.findAll();
    }

    @Override
    public Book getBookById(UUID id) {
        log.info("Fetching book by id: {}", id);
        return bookDao.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id while getting: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    @Override
    public List<Book> searchBooksByName(String name) {
        log.info("Searching books by name: {}", name);
        return bookDao.findAll().stream()
                .filter(book -> book.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .toList();
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        log.info("Fetching books by author: {}", author);
        return bookDao.findAll().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().equals(author))
                .toList();
    }

    @Override
    public boolean rentBook(UUID bookId, UUID userId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return Optional.of(book)
                .filter(b -> b.getAvailableCount() > 0)
                .map(b -> {
                    b.setAvailableCount(b.getAvailableCount() - 1);
                    bookDao.save(b);

                    user.getBooks().add(b);
                    userDao.save(user);

                    Rent rent = new Rent();
                    rent.setBook(b);
                    rent.setUser(user);
                    rent.setRentedAt(LocalDateTime.now());
                    rentDao.save(rent);

                    return true;
                })
                .orElse(false);
    }


    @Override
    public boolean returnBook(UUID bookId, UUID userId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return rentDao.findByUserAndBookAndReturnedAtIsNull(userId, bookId)
                .map(rent -> {
                    rent.setReturnedAt(LocalDateTime.now());
                    rentDao.save(rent);

                    book.setAvailableCount(book.getAvailableCount() + 1);
                    bookDao.save(book);

                    return true;
                })
                .orElseThrow(() -> new RentNotFoundException(
                        "Rent not found for book " + bookId + " and user " + userId));
    }

    @Override
    public List<Book> getAvailableBooks() {
        log.info("Fetching available books");
        return bookDao.findAll().stream()
                .filter(book -> book.getAvailableCount() > 0)
                .toList();
    }

    @Override
    public Book updateBook(UUID id, Book book) {
        log.info("Updating book with id: {}", id);
        return bookDao.findById(id)
                .map(existingBook -> {
                    book.setId(id);
                    bookDao.update(book);
                    log.info("Book updated successfully with id: {}", id);
                    return book;
                })
                .orElseThrow(() -> {
                    log.error("Book not found with id while updating: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    @Override
    public Book updateAvailableCount(UUID id, int count) {
        log.info("Updating available count for book id: {} to {}", id, count);
        return bookDao.findById(id)
                .map(book -> {
                    book.setAvailableCount(count);
                    bookDao.update(book);
                    log.info("Available count updated successfully for book id: {}", id);
                    return book;
                })
                .orElseThrow(() -> {
                    log.error("Book not found with id while update availability: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    @Override
    public void deleteBook(UUID id) {
        log.info("Deleting book with id: {}", id);
        bookDao.findById(id)
                .ifPresentOrElse(
                        book -> {
                            bookDao.delete(id);
                            log.info("Book deleted successfully with id: {}", id);
                        },
                        () -> {
                            log.error("Book not found with id while deleting: {}", id);
                            throw new BookNotFoundException("Book not found with id: " + id);
                        }
                );
    }
}