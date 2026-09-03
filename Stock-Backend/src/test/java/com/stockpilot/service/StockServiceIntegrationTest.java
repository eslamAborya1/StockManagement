package com.stockpilot.service;

import com.stockpilot.dto.ProductDTO;
import com.stockpilot.dto.StockMovementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class StockServiceIntegrationTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Test
    void testRecordMovementUpdatesStockSuccessfully() {
        // Create product
        String sku = "SKU-" + UUID.randomUUID().toString().substring(0, 8);
        ProductDTO productDTO = new ProductDTO(null, "Test Product", sku, BigDecimal.valueOf(100), 0, null);
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
        String sku = "SKU-" + UUID.randomUUID().toString().substring(0, 8);
        ProductDTO productDTO = new ProductDTO(null, "Test Product 2", sku, BigDecimal.valueOf(100), 10, null);
        ProductDTO savedProduct = productService.createProduct(productDTO);

        StockMovementDTO outMovement = new StockMovementDTO(null, savedProduct.getId(), 20, "OUT", LocalDateTime.now());
        
        assertThatThrownBy(() -> stockService.recordMovement(outMovement))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient stock");
    }
}
