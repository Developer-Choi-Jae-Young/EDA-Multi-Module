package org.example.websocket.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.BuyException;
import org.example.websocket.common.exception.ExistProductException;
import org.example.websocket.order.dto.request.BuyOrderDto;
import org.example.websocket.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderContoller {
    private final OrderService orderService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyOrder(@RequestBody BuyOrderDto buyOrderDto) throws BuyException, ExistProductException {
        org.example.websocket.order.dto.response.BuyOrderDto response =
                org.example.websocket.order.dto.response.BuyOrderDto.of(orderService.buy(buyOrderDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
