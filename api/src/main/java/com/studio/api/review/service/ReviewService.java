package com.studio.api.review.service;

import static com.studio.api.review.service.serializer.ReviewSerializer.toReviewEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.REVIEW_EXIST;
import static com.studio.core.global.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.TICKET_NOT_USED;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.review.dto.ProductReviewRequestDto;
import com.studio.core.review.entity.ReviewEntity;
import com.studio.core.review.repository.ReviewJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewJpaRepository reviewRepository;
    private final OrderJpaRepository orderRepository;
    private final TicketJpaRepository ticketRepository;

    public Long countUnReviewByMember(MemberAuthEntity member) {
        return reviewRepository.countUnreviewedUsedTicketsByMember(member, TicketUsedState.USED);
    }
    public void insertReview(Long orderId, ProductReviewRequestDto req, MemberAuthEntity member) {

        if (reviewRepository.existsByOrderIdAndMemberNo(orderId, member.getMemberNo())) {
            throw new CustomException(REVIEW_EXIST);

        }

        TicketEntity ticket = ticketRepository.findByOrder_id(orderId).orElseThrow(()-> new CustomException(TICKET_NOT_USED));

        if(!TicketUsedState.USED.equals(ticket.getIsUsed())){
            throw new CustomException(TICKET_NOT_USED);

        }

        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(
            ORDER_NOT_FOUND));
        if (!Channels.TRAVELIDGE.equals(order.getProduct().getChannel())) {
            throw new CustomException(NOT_EXPECT_CHANNEL);
        }
        if (!order.getMember().getMemberNo().equals(member.getMemberNo())) {
            throw new CustomException(NOT_VALID_MEMBER);
        }
        ReviewEntity review = toReviewEntity(req, order.getProduct(), order, member);

        reviewRepository.save(review);

    }

    public void deleteReview(Long reviewId, MemberAuthEntity member) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        if (!review.getMember().getMemberNo().equals(member.getMemberNo())) {
            throw new CustomException(NOT_VALID_MEMBER);

        }
        reviewRepository.deleteById(reviewId);
    }
}
