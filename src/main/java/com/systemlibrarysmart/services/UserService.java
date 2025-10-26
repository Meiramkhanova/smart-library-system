package com.systemlibrarysmart.services;

import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.domain.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);

    List<Book> getUserRentedBooks(UUID userId);

    List<User> getAllUsers();

    User getUserById(UUID id);

    User updateUserById(UUID id, User user);

    void deleteUserById(UUID id);
}