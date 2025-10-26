package com.systemlibrarysmart.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String surname;
    private String firstname;
    private String email;
    private String password;
    private int age;
    @JsonIgnore
    private List<Book> books = new ArrayList<>();
}


