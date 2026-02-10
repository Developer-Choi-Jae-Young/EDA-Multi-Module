package org.example.websocket.common.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultHolder<T> {
    private T value;
}
