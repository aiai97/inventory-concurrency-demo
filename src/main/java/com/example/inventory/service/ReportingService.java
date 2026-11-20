package com.example.inventory.service;

import com.example.inventory.domain.Product;
import com.example.inventory.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportingService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ReportingService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int readStockReadUncommitted(String sku) {
        Product product = productRepository.findBySku(sku).orElseThrow();
        return product.getStock();
    }

    @Transactional
    public void updateStockWithoutCommit(String sku, int delta) {
        Product product = productRepository.findBySku(sku).orElseThrow();
        product.setStock(product.getStock() + delta);
        productRepository.flush();
        sleep(10000);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int nonRepeatableRead(String sku, long delayMillis) {
        Product p1 = productRepository.findBySku(sku).orElseThrow();
        int first = p1.getStock();
        System.out.println("first read"+first);
        sleep(delayMillis);
        entityManager.refresh(p1);
        Product p2 = productRepository.findBySku(sku).orElseThrow();
        int second = p2.getStock();
        System.out.println("second read"+second);
        return second - first;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public long phantomReadCount(long delayMillis) {
        long count1 = productRepository.count();
        System.out.println("first count"+count1);
        sleep(delayMillis);
        long count2 = productRepository.count();
        System.out.println("second count"+count2);
        return count2 - count1;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
