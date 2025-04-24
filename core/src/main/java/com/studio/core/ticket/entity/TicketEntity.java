package com.studio.core.ticket.entity;


import com.studio.core.global.enums.converter.TicketUsedStateConverter;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Entity
@Table(name = "TICKET")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
public class TicketEntity extends TimeBaseEntity {

    @Id
    @Comment("티켓 키")
    private String ticketKey;

    @Comment("사용여부")
    @Convert(converter = TicketUsedStateConverter.class)
    private TicketUsedState isUsed;

    @Comment("사용일")
    private LocalDateTime usedAt;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberAuthEntity member;

    public void updateTicketState(TicketUsedState state) {
        this.isUsed = state;
    }

    public void useTicket(String ticketKey, TicketUsedState usedState){
        this.isUsed = usedState;
        this.ticketKey = ticketKey;
        this.usedAt = LocalDateTime.now();

    }

}
