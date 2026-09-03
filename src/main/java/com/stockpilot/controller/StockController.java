package com.stockpilot.controller;

import com.stockpilot.dto.ProductDTO;
import com.stockpilot.dto.StockMovementDTO;
import com.stockpilot.service.ProductService;
import com.stockpilot.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {

    private final StockService stockService;
    private final ProductService productService;

    public StockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovementDTO>> getAllMovements() {
        return ResponseEntity.ok(stockService.getAllMovements());
    }

    @GetMapping("/movements/product/{productId}")
    public ResponseEntity<List<StockMovementDTO>> getMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getMovementsByProduct(productId));
    }

    @PostMapping("/movements")
    public ResponseEntity<StockMovementDTO> recordMovement(@Valid @RequestBody StockMovementDTO movementDTO) {
        StockMovementDTO recordedMovement = stockService.recordMovement(movementDTO);
        return new ResponseEntity<>(recordedMovement, HttpStatus.CREATED);
    }

    @GetMapping("/reports/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockReport(@RequestParam(defaultValue = "10") Integer threshold) {
        // We'll add this method to ProductService right after this
        // but for now let's just assume it exists or call the repo directly if we were in a hurry.
        // I'll update ProductService accordingly.
        return ResponseEntity.ok(productService.getLowStockProducts(threshold));
    }
}
