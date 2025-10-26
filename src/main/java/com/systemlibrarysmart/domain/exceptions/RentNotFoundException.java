package com.systemlibrarysmart.domain.exceptions;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(String message) {
        super(message);
    }
}
