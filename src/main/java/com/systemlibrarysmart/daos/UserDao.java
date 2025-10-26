package com.systemlibrarysmart.daos;

import com.systemlibrarysmart.domain.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    List<User> findAll();
    Optional<User> findById(UUID id);
    void save(User user);
    void update(User user);
    void delete(UUID id);
    boolean notExistByEmail(String email);
}
