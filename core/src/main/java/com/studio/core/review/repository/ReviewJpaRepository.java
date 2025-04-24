package com.studio.core.review.repository;


import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT COUNT(r) > 0 FROM ReviewEntity r WHERE r.order.id = :orderId AND r.member.memberNo = :memberNo")
    boolean existsByOrderIdAndMemberNo(@Param("orderId") Long orderId, @Param("memberNo") Long memberNo);

    int countByProductId(Long productId);


    int countByMember(MemberAuthEntity member);

    @Query("""
    SELECT COUNT(o)
    FROM OrderEntity o
    JOIN TicketEntity t ON o.id = t.order.id
    LEFT JOIN ReviewEntity r ON o.id = r.order.id
    WHERE o.member = :member
    AND t.isUsed = :usedState
    AND r.id IS NULL
    """)
    Long countUnreviewedUsedTicketsByMember(@Param("member") MemberAuthEntity member,
        @Param("usedState") TicketUsedState usedState);

}
