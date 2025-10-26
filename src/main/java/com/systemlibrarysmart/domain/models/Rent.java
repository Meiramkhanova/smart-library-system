package com.systemlibrarysmart.domain.models;


import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rent {
    private UUID id;
    private Book book;
    private User user;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
}