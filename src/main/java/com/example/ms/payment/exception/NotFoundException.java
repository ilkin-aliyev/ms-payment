package com.example.ms.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {
    private String code;

    public NotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
