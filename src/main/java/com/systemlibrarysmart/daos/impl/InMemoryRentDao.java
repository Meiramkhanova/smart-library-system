package com.systemlibrarysmart.daos.impl;

import com.systemlibrarysmart.daos.RentDao;
import com.systemlibrarysmart.domain.models.Book;
import com.systemlibrarysmart.domain.models.Rent;
import com.systemlibrarysmart.domain.models.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryRentDao implements RentDao {

    private final Map<UUID, Rent> rents = new HashMap<>();

    @Override
    public List<Rent> findAll() {
        return new ArrayList<>(rents.values());
    }

    @Override
    public Optional<Rent> findById(UUID id) {
        return Optional.ofNullable(rents.get(id));
    }

    @Override
    public void save(Rent rent) {
        if (rent.getId() == null) {
            rent.setId(UUID.randomUUID());
        }
        rents.put(rent.getId(), rent);
    }

    @Override
    public void update(Rent rent) {
        if (rent.getId() != null && rents.containsKey(rent.getId())) {
            rents.put(rent.getId(), rent);
        }
    }

    @Override
    public void delete(UUID id) {
        rents.remove(id);
    }

    @Override
    public List<Rent> findByUserAndReturnedAtIsNull(User user) {
        return rents.values().stream()
                .filter(rent -> rent.getUser().equals(user) && rent.getReturnedAt() == null)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Rent> findByUserAndBookAndReturnedAtIsNull(UUID userId, UUID bookId) {
        return rents.values().stream()
                .filter(rent ->
                        rent.getUser().getId().equals(userId)
                                && rent.getBook().getId().equals(bookId)
                                && rent.getReturnedAt() == null)
                .findFirst();
    }
}
