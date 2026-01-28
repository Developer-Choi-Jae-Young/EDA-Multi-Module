package org.example.websocket.common.exception;

import lombok.Getter;

@Getter
public class BuyException extends RuntimeException {
    private final int errorCode;

    public BuyException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
