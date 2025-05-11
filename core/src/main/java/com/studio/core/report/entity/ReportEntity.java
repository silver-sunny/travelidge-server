package com.studio.core.report.entity;


import com.studio.core.global.enums.ReportTargetType;
import com.studio.core.inquiry.entity.InquiryEntity;
import com.studio.core.member.entity.MemberAuthEntity;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "report")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("신고 번호")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    @Comment("신고자")
    private MemberAuthEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("신고 멤버")
    private MemberAuthEntity target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("신고 리뷰")
    private InquiryEntity inquiry;

    @Column(nullable = false, length = 300)
    @Comment("신고 사유")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("신고 대상 타입")
    private ReportTargetType targetType;

    @CreatedDate
    @Column(updatable = false)
    @Comment("신고 일시")
    private LocalDateTime reportedAt;
}