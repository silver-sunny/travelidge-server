package com.studio.core.inquiry.entity;


import com.studio.core.global.enums.inquiry.InquiryPrivateState;
import com.studio.core.global.enums.inquiry.InquiryResolvedState;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.product.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Entity
@Table(name = "INQUIRY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class InquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("문의 번호")
    private Long id;

    @Comment("문의")
    private String inquiry;

    @Comment("답변")
    private String answer;

    @Comment("문의 날짜")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime inquiryAt;

    @Comment("답변 날짜")
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime answerAt;

    @Comment("비공개 여부")
    @Enumerated(EnumType.ORDINAL)
    private InquiryPrivateState isPrivate;

    @Comment("해결 여부")
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private InquiryResolvedState isResolved = InquiryResolvedState.NOT_RESOLVED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private MemberAuthEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;

    public void updateAnswer(String answer) {
        this.isResolved = InquiryResolvedState.RESOLVED;
        this.answer = answer;
        this.answerAt = LocalDateTime.now();
    }

}