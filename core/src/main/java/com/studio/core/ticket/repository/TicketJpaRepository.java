package com.studio.core.ticket.repository;

import com.studio.core.ticket.entity.TicketEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, String> {


    @EntityGraph(attributePaths = {"order"})
    Optional<TicketEntity> findByTicketKey(String ticketKey);


    Optional<TicketEntity> findByOrder_id(Long orderId);
}
