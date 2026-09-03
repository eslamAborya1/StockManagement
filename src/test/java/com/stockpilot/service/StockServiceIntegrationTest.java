package com.stockpilot.service;

import com.stockpilot.dto.ProductDTO;
import com.stockpilot.dto.StockMovementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
class StockServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18");

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Test
    void testRecordMovementUpdatesStockSuccessfully() {
        // Create product
        ProductDTO productDTO = new ProductDTO(null, "Test Product", "SKU-123", BigDecimal.valueOf(100), 0, null);
        ProductDTO savedProduct = productService.createProduct(productDTO);

        // Record IN movement
        StockMovementDTO inMovement = new StockMovementDTO(null, savedProduct.getId(), 50, "IN", LocalDateTime.now());
        stockService.recordMovement(inMovement);

        ProductDTO updatedProduct = productService.getProductById(savedProduct.getId());
        assertThat(updatedProduct.getCurrentStock()).isEqualTo(50);

        // Record OUT movement
        StockMovementDTO outMovement = new StockMovementDTO(null, savedProduct.getId(), 20, "OUT", LocalDateTime.now());
        stockService.recordMovement(outMovement);

        updatedProduct = productService.getProductById(savedProduct.getId());
        assertThat(updatedProduct.getCurrentStock()).isEqualTo(30);
    }

    @Test
    void testRecordOutMovementWithInsufficientStockThrowsException() {
        ProductDTO productDTO = new ProductDTO(null, "Test Product 2", "SKU-124", BigDecimal.valueOf(100), 10, null);
        ProductDTO savedProduct = productService.createProduct(productDTO);

        StockMovementDTO outMovement = new StockMovementDTO(null, savedProduct.getId(), 20, "OUT", LocalDateTime.now());
        
        assertThatThrownBy(() -> stockService.recordMovement(outMovement))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient stock");
    }
}
