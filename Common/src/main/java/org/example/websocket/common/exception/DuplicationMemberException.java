package org.example.websocket.common.exception;

import lombok.Getter;

@Getter
public class DuplicationMemberException extends Exception {
    private final int errorCode;

    public DuplicationMemberException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
