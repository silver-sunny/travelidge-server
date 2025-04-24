package com.studio.core.order.repository;

import com.studio.core.order.entity.payment.PaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    @EntityGraph(attributePaths = {"member", "product"})
    Optional<PaymentEntity> findByChannelProductOrderId(String orderId);

}
