package org.example.websocket.common.exception;

import lombok.Getter;

@Getter
public class ExistProductException extends Exception {
    private final int errorCode;

    public ExistProductException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
