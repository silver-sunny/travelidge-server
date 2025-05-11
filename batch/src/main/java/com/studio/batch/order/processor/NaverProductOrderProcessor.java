package com.studio.batch.order.processor;

import static com.studio.batch.order.service.serializer.OrderSerializer.toOrderEntity;

import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverProductOrderProcessor implements ItemProcessor<NaverDataDto, OrderEntity> {


    private final ProductJpaRepository productRepository;

    private final OrderJpaRepository orderRepository;


    @Override
    public OrderEntity process(NaverDataDto naverDataDto) {
        return orderRepository.findByChannelProductOrderId(naverDataDto.getProductOrder().getProductOrderId())
            .map(order -> {
                order.updateOrder(naverDataDto);
                return order;
            })
            .orElseGet(() -> productRepository.findByChannelProductId(naverDataDto.getProductOrder().getProductId())
                .map(product -> toOrderEntity(naverDataDto, product))
                .orElse(null));
    }

}
