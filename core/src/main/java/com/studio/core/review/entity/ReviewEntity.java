package com.studio.core.review.entity;

import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.product.entity.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Entity
@Table(
    name = "REVIEW",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "member_no"})
    }
)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
public class ReviewEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("리뷰내용")
    private String content; // 리뷰 내용

    @Min(1)
    @Max(5)
    @Comment("별졈")
    private int rating;

    @Comment("리뷰 이미지")
    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImageEntity> ri = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberAuthEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;


    public void updateRi(List<String> ri) {
        this.ri = ri.stream().map(img -> new ReviewImageEntity(img, this)).toList();

    }
}
