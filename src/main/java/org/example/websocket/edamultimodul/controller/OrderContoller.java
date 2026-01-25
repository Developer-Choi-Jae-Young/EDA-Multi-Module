package org.example.websocket.edamultimodul.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.BuyOrderDto;
import org.example.websocket.edamultimodul.entity.OrderEntity;
import org.example.websocket.edamultimodul.exception.BuyException;
import org.example.websocket.edamultimodul.exception.ExistProductException;
import org.example.websocket.edamultimodul.service.OrderService;
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
        org.example.websocket.edamultimodul.dto.response.BuyOrderDto response =
                org.example.websocket.edamultimodul.dto.response.BuyOrderDto.of(orderService.buy(buyOrderDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
