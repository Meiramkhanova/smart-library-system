package com.systemlibrarysmart.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private UUID id;
    private String name;
    private String author;
    private int countOfPages;
    private String description;
    private int availableCount;
    private String isbn;
}

