package org.example.websocket.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.ExistProductException;
import org.example.websocket.inventory.dto.response.InventoryListDto;
import org.example.websocket.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertInventory(@RequestBody org.example.websocket.inventory.dto.request.InsertInventoryDto insertInventoryDto) throws ExistProductException {
        org.example.websocket.inventory.dto.response.InsertInventoryDto response =
                org.example.websocket.inventory.dto.response.InsertInventoryDto.of(inventoryService.insertInventory(insertInventoryDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getInventoryList() {
        List<InventoryListDto> response = inventoryService.getInventoryList().stream().map(InventoryListDto::of).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
