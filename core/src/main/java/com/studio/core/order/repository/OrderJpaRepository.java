package com.studio.core.order.repository;


import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.CancelOrReturnState;
import com.studio.core.global.enums.order.ProductOrderState;
import com.studio.core.global.enums.order.TicketState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @EntityGraph(attributePaths = {"member","product"})
    Optional<OrderEntity> findById(Long id);

    Optional<OrderEntity> findByChannelProductOrderId(String orderId);

    @Transactional
    @Modifying
    @Query("UPDATE OrderEntity po "
            + "SET po.ticketState = :ticketState, "
            + "po.orderState = :orderState "
            + "WHERE po.id = :id")
    void updateProductOrderOfTicket(Long id, TicketState ticketState, ProductOrderState orderState);

    @Query("SELECT COUNT(*) " +
            "FROM OrderEntity po " +
            "WHERE po.cancelOrReturnState IN :cancelOrReturnStates")
    int countProductOrderByCancelOrReturnStates(List<CancelOrReturnState> cancelOrReturnStates);


    @Query("SELECT p FROM OrderEntity p " +
        "WHERE p.ticketState = :ticketState " +
        "AND p.isProgressing = false " +
        "AND p.channel = :channel " +
        "AND p.orderState IN :orderStates " +
        "AND (p.cancelOrReturnState IS NULL OR p.cancelOrReturnState IN :cancelOrReturnStates)")
    List<OrderEntity> findNonIssuedTickets(
        @Param("ticketState") TicketState ticketState,
        @Param("channel") Channels channel,
        @Param("orderStates") List<ProductOrderState> orderStates,
        @Param("cancelOrReturnStates") List<CancelOrReturnState> cancelOrReturnStates
    );

    @Query("SELECT COUNT(*) " +
        "FROM OrderEntity o " +
        "WHERE o.member = :member ")
    Long countByMemberAndOrderState(
        @Param("member")MemberAuthEntity member);
}
