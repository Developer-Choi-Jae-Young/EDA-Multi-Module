package org.example.websocket.edamultimodul.exception;

import lombok.Getter;

@Getter
public class BuyException extends Exception {
    private final int errorCode;

    public BuyException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
