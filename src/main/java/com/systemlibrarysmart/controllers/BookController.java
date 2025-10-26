package com.systemlibrarysmart.controllers;

import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByName(@RequestParam String name) {
        return ResponseEntity.ok(bookService.searchBooksByName(name));
    }

    @GetMapping("/authors/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @PostMapping("/{bookId}/rent/{userId}")
    public ResponseEntity<String> rentBook(
            @PathVariable UUID bookId,
            @PathVariable UUID userId) {

        boolean success = bookService.rentBook(bookId, userId);

        if (success) {
            return ResponseEntity.ok("Book rented successfully.");
        } else {
            return ResponseEntity.badRequest().body("Book not available or user not found.");
        }
    }

    @PostMapping("/{bookId}/return/{userId}")
    public ResponseEntity<String> returnBook(
            @PathVariable UUID bookId,
            @PathVariable UUID userId) {

        boolean success = bookService.returnBook(bookId, userId);

        if (success) {
            return ResponseEntity.ok("Book returned successfully.");
        } else {
            return ResponseEntity.badRequest().body("Rent record not found or already returned.");
        }
    }


    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Book> updateBookAvailability(
            @PathVariable UUID id,
            @RequestParam int count) {
        return ResponseEntity.ok(bookService.updateAvailableCount(id, count));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}