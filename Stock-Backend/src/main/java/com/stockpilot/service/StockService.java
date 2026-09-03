package com.stockpilot.service;

import com.stockpilot.dto.StockMovementDTO;
import com.stockpilot.entity.Product;
import com.stockpilot.entity.StockMovement;
import com.stockpilot.repository.ProductRepository;
import com.stockpilot.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public StockService(StockMovementRepository stockMovementRepository, ProductRepository productRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
    }

    public List<StockMovementDTO> getAllMovements() {
        return stockMovementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementDTO> getMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProductIdOrderByMovementDateDesc(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StockMovementDTO recordMovement(StockMovementDTO movementDTO) {
        Product product = productRepository.findById(movementDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + movementDTO.getProductId()));

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setQuantity(movementDTO.getQuantity());
        movement.setType(movementDTO.getType());
        movement.setMovementDate(movementDTO.getMovementDate() != null ? movementDTO.getMovementDate() : LocalDateTime.now());

        // Update product stock
        if ("IN".equals(movement.getType())) {
            product.setCurrentStock(product.getCurrentStock() + movement.getQuantity());
        } else if ("OUT".equals(movement.getType())) {
            if (product.getCurrentStock() < movement.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product id: " + product.getId());
            }
            product.setCurrentStock(product.getCurrentStock() - movement.getQuantity());
        } else {
            throw new IllegalArgumentException("Invalid movement type");
        }

        productRepository.save(product);
        StockMovement savedMovement = stockMovementRepository.save(movement);
        return mapToDTO(savedMovement);
    }

    private StockMovementDTO mapToDTO(StockMovement movement) {
        return new StockMovementDTO(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getQuantity(),
                movement.getType(),
                movement.getMovementDate()
        );
    }
}
