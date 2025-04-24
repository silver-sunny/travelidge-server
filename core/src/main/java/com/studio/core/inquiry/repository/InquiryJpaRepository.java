package com.studio.core.inquiry.repository;

import com.studio.core.inquiry.entity.InquiryEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InquiryJpaRepository extends JpaRepository<InquiryEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE InquiryEntity i "
            + "SET i.answer = :#{#inquiry.getAnswer()},"
            + "i.isResolved = :#{#inquiry.getIsResolved()}, "
            + "i.answerAt = :#{#inquiry.getAnswerAt()} "
            + "WHERE i.id = :#{#inquiry.getId()}")
    void insertAnswer(InquiryEntity inquiry);

    @EntityGraph(attributePaths = {"member", "product"})
    Optional<InquiryEntity> findById(Long id);


    int countByProductId(Long productId);
}
