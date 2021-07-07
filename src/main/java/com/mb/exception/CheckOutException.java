package com.mb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckOutException extends RuntimeException {

    public CheckOutException() {
        super("Insufficient data passed");
    }
}
