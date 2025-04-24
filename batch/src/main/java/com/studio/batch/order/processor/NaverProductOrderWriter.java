package com.studio.batch.order.processor;

import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverProductOrderWriter implements ItemWriter<OrderEntity> {

    private final OrderJpaRepository orderRepository;

    @Override
    public void write(Chunk<? extends OrderEntity> chunk) {
        orderRepository.saveAll(chunk);
    }
}

