package com.studio.api.review.service;

import static com.studio.api.review.service.serializer.ReviewSerializer.toReviewEntity;
import static com.studio.core.global.exception.ErrorCode.NOT_EXPECT_CHANNEL;
import static com.studio.core.global.exception.ErrorCode.NOT_VALID_MEMBER;
import static com.studio.core.global.exception.ErrorCode.ORACLE_BUCKET_FAIL;
import static com.studio.core.global.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.REVIEW_EXIST;
import static com.studio.core.global.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.studio.core.global.exception.ErrorCode.TICKET_NOT_USED;

import com.studio.api.image.OracleStorageService;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.order.TicketUsedState;
import com.studio.core.global.exception.CustomException;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.order.repository.OrderJpaRepository;
import com.studio.core.review.dto.ProductReviewRequestDto;
import com.studio.core.review.entity.ReviewEntity;
import com.studio.core.review.entity.ReviewImageEntity;
import com.studio.core.review.repository.ReviewJpaRepository;
import com.studio.core.ticket.entity.TicketEntity;
import com.studio.core.ticket.repository.TicketJpaRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewJpaRepository reviewRepository;
    private final OrderJpaRepository orderRepository;
    private final TicketJpaRepository ticketRepository;
    private final OracleStorageService storageService;

    public Long countUnReviewByMember(MemberAuthEntity member) {
        return reviewRepository.countUnreviewedUsedTicketsByMember(member, TicketUsedState.USED);
    }

    @Transactional
    public void insertReview(Long orderId, ProductReviewRequestDto req, List<MultipartFile> files,
        MemberAuthEntity member) {
        List<String> uploadedFileUrls = new ArrayList<>();

        try {
            if (reviewRepository.existsByOrderIdAndMemberNo(orderId, member.getMemberNo())) {
                throw new CustomException(REVIEW_EXIST);
            }

            TicketEntity ticket = ticketRepository.findByOrder_id(orderId)
                .orElseThrow(() -> new CustomException(TICKET_NOT_USED));

            if (!TicketUsedState.USED.equals(ticket.getIsUsed())) {
                throw new CustomException(TICKET_NOT_USED);
            }

            OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

            if (!Channels.TRAVELIDGE.equals(order.getProduct().getChannel())) {
                throw new CustomException(NOT_EXPECT_CHANNEL);
            }

            if (!order.getMember().getMemberNo().equals(member.getMemberNo())) {
                throw new CustomException(NOT_VALID_MEMBER);
            }

            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    try {
                        String uploadedUrl = storageService.upload(file, member);
                        uploadedFileUrls.add(uploadedUrl);
                    } catch (IOException e) {
                        throw new CustomException(ORACLE_BUCKET_FAIL, e);
                    }
                }
            }

            ReviewEntity review = toReviewEntity(req, uploadedFileUrls, order.getProduct(), order,
                member);

            reviewRepository.save(review);

        } catch (Exception e) {
            // ⭐ 리뷰 저장 실패시 업로드된 파일 삭제
            for (String uploadedUrl : uploadedFileUrls) {
                try {
                    String[] segments = uploadedUrl.split("/");
                    String lastSegment = segments[segments.length - 1];
                    storageService.delete(lastSegment, member);
                } catch (Exception deleteEx) {
                    // 삭제 실패해도 로깅만
                    log.error("파일 삭제 실패: " + uploadedUrl, deleteEx);
                }
            }
            throw new RuntimeException(e);

        }
    }


    @Transactional
    public void deleteReview(Long reviewId, MemberAuthEntity member) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        if (!review.getMember().getMemberNo().equals(member.getMemberNo())) {
            throw new CustomException(NOT_VALID_MEMBER);
        }

        List<String> fileKeys = new ArrayList<>();
        if (review.getRi() != null) {
            for (ReviewImageEntity uploadedUrl : review.getRi()) {
                String[] segments = uploadedUrl.getImageUrl().split("/");
                String lastSegment = segments[segments.length - 1];
                fileKeys.add(lastSegment);
            }
        }

        reviewRepository.deleteById(reviewId);

        // 트랜잭션 커밋 이후 파일 삭제
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (String fileKey : fileKeys) {
                    try {
                        storageService.delete(fileKey, member);
                    } catch (Exception e) {
                        log.error("파일 삭제 실패: {}", fileKey, e);
                    }
                }
            }
        });
    }
}
