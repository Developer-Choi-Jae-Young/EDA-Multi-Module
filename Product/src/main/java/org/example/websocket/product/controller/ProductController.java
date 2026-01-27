package org.example.websocket.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.product.dto.request.InsertProductDto;
import org.example.websocket.product.dto.response.ProductListDto;
import org.example.websocket.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertProduct(@RequestBody InsertProductDto insertProductDto) {
        org.example.websocket.product.dto.response.InsertProductDto response =
                org.example.websocket.product.dto.response.InsertProductDto.of(productService.insertProduct(InsertProductDto.of(insertProductDto)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProductList() {
        List<ProductListDto> response = productService.getProductList().stream().map(ProductListDto::of).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
