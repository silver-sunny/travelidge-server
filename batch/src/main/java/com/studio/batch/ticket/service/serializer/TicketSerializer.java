package com.studio.batch.ticket.service.serializer;

import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.ticket.entity.TicketEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TicketSerializer {

    public static TicketEntity toTicketEntity(String ticketKey, TicketUsedState isUsed,
        LocalDateTime usedAt, OrderEntity order) {
        return TicketEntity.builder()
            .ticketKey(ticketKey)
            .isUsed(isUsed)
            .usedAt(usedAt)
            .order(order)
            .build();

    }


}
