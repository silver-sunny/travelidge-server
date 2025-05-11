package com.studio.core.product.entity;


import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
        name = "CARTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_product_member", columnNames = {"productId", "memberNo"})
        }
)
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CartEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("장바구니 아이디")
    private Long id;

    @Comment("구매수량")
    private int purchaseQuantity;

    @Comment("옵션 직접입력 ( 예약날짜 )")
    private String directOption;

    @Comment("주문서 활성화 여부")
    private boolean isActiveOrderForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private MemberAuthEntity member;

    public void updateCart(int purchaseQuantity, String directOption){
        this.isActiveOrderForm = true;
        this.purchaseQuantity = purchaseQuantity;
        this.directOption = directOption;
    }


}
