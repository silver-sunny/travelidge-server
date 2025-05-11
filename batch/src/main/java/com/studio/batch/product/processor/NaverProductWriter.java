package com.studio.batch.product.processor;

import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverProductWriter implements ItemWriter<ProductEntity> {

    private final ProductJpaRepository productRepository;  // JPA Repository

    @Override
    public void write(Chunk<? extends ProductEntity> chunk) throws Exception {
        try {
            // 전체 수정
            productRepository.saveAll(chunk.getItems());
        } catch (Exception e) {
            log.error("Error while saving product orders: {}", e.getMessage(), e);
            throw new RuntimeException("Error while saving product orders", e);
        }
    }
}



