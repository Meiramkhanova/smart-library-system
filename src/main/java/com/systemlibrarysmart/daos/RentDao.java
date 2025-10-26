package com.systemlibrarysmart.daos;

import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.domain.models.Rent;
import com.systemlibrarysmart.domain.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentDao {
    List<Rent> findAll();
    Optional<Rent> findById(UUID id);
    void save(Rent rent);
    void update(Rent rent);
    void delete(UUID id);
    List<Rent> findByUserAndReturnedAtIsNull(User user);
    Optional<Rent> findByUserAndBookAndReturnedAtIsNull(UUID userId, UUID bookId);
}
