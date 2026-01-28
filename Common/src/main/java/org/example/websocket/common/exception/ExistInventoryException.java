package org.example.websocket.common.exception;

import lombok.Getter;

@Getter
public class ExistInventoryException extends RuntimeException {
    private final int errorCode;

    public ExistInventoryException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
