package com.systemlibrarysmart.daos.impl;

import com.systemlibrarysmart.daos.UserDao;
import com.systemlibrarysmart.domain.models.User;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class InMemoryUserDao implements UserDao {

    private static final List<User> USERS = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return USERS;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return USERS.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(User user) {
        USERS.add(user);
    }

    @Override
    public void update(User user) {
        Optional<User> existingUser = USERS.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst();

        if (existingUser.isPresent()) {
            int index = USERS.indexOf(existingUser.get());
            USERS.set(index, user);
        } else {
            throw new NoSuchElementException("User with id " + user.getId() + " not found");
        }
    }
    @Override
    public void delete(UUID id) {
        boolean removed = USERS.removeIf(u -> u.getId().equals(id));
        if (!removed) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
    }

    @Override
    public boolean notExistByEmail(String email) {
        return USERS.stream()
                .noneMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}

