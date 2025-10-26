package com.systemlibrarysmart.services.impl;

import com.systemlibrarysmart.daos.RentDao;
import com.systemlibrarysmart.daos.UserDao;
import com.systemlibrarysmart.domain.exceptions.UserAlreadyExistsException;
import com.systemlibrarysmart.domain.exceptions.UserNotFoundException;
import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.domain.models.Rent;
import com.systemlibrarysmart.domain.models.User;
import com.systemlibrarysmart.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RentDao rentDao;

    @Override
    public User createUser(User user) {
        log.info("Creating user with email: {}", user.getEmail());

        if (userDao.notExistByEmail(user.getEmail())) {
            user.setId(UUID.randomUUID());
            userDao.save(user);
        } else {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        return user;
    }

    @Override
    public List<Book> getUserRentedBooks(UUID userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Rent> rents = rentDao.findByUserAndReturnedAtIsNull(user);
        return rents.stream()
                .map(Rent::getBook)
                .toList();
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userDao.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        log.info("Fetching user by id: {}", id);
        return userDao.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id while getting: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
    }

    @Override
    public User updateUserById(UUID id, User user) {
        log.info("Updating user with id: {}", id);
        return userDao.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    userDao.update(user);
                    log.info("User updated successfully with id: {}", id);
                    return user;
                })
                .orElseThrow(() -> {
                    log.error("User not found with id while updating: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });
    }

    @Override
    public void deleteUserById(UUID id) {
        log.info("Deleting user with id: {}", id);

        userDao.findById(id)
                .ifPresentOrElse(
                        user -> {
                            userDao.delete(id);
                            log.info("User deleted successfully with id: {}", id);
                        },
                        () -> {
                            log.error("User not found with id while deleting: {}", id);
                            throw new UserNotFoundException("User not found with id: " + id);
                        }
                );
    }

}