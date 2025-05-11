package com.studio.core.product.entity;


import static com.studio.core.global.utils.ProductUtil.getValueFromDiscountPolicy;

import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.enums.converter.ChannelsConverter;
import com.studio.core.global.enums.converter.ProductStateConverter;
import com.studio.core.global.naver.dto.NaverChannelProductsDto;
import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.entity.MemberAuthEntity;
import com.studio.core.order.entity.OrderEntity;
import com.studio.core.product.dto.product.ProductRequestDto;
import com.studio.core.product.dto.product.naver.NaverImagesDto;
import com.studio.core.product.dto.product.naver.NaverOriginProductDto;
import com.studio.core.product.dto.product.naver.NaverProductDto;
import com.studio.core.product.dto.product.naver.NaverProductImageDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@DynamicUpdate
public class ProductEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 번호")
    private Long id;

    @Comment("상품명")
    private String productName;

    @Comment("가격")
    private Long price;

    @Comment("할인율")
    private Long discountRate;

    @Comment("재고")
    private Long stock;

    @Comment("주문상태")
    @Convert(converter = ProductStateConverter.class)
    private ProductState productState;

    @Comment("대표이미지")
    private String pri;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> pdi = new ArrayList<>();

    @Comment("설명")
    @Lob
    private String description;

    @Comment("채널")
    @Convert(converter = ChannelsConverter.class)
    private Channels channel;


    @Comment("채널 상품 키값")
    private String channelProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberAuthEntity registrant;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<OrderEntity> orders;

    public void updateProduct(ProductRequestDto dto) {
        this.productName = dto.productName();
        this.price = dto.price();
        this.discountRate = dto.discountRate();
        this.pri = dto.pri();
        this.pdi = dto.pdi().stream().map(img -> new ProductImageEntity(img, this)).toList();

        this.description = dto.description();
        this.productState = dto.productState();
        if (ProductState.OUTOFSTOCK.equals(this.productState)) {
            this.stock = 0L;
        } else {
            this.stock = dto.stock();
        }

    }

    public void updateProduct(NaverProductDto dto) {
        NaverOriginProductDto naverOriginProduct = dto.getOriginProduct();
        NaverImagesDto naverImagesDto = naverOriginProduct.getImages();
        List<NaverProductImageDto> pdi = naverImagesDto.getOptionalImages().stream().toList();

        this.productName = naverOriginProduct.getName();
        this.price = naverOriginProduct.getSalePrice();
        this.discountRate = getValueFromDiscountPolicy(naverOriginProduct.getCustomerBenefit());
        this.stock = naverOriginProduct.getStockQuantity();
        this.pri = naverImagesDto.getRepresentativeImage().getUrl();
        this.pdi = pdi.stream()
            .map(imageDto -> new ProductImageEntity(imageDto.getUrl(),
                this)) // ProductImageEntity 생성
            .toList();
        this.description = naverOriginProduct.getDetailContent();
        this.productState = ProductState.getProductStateByNaverProductStatusEnum(
            naverOriginProduct.getStatusType());
    }

    public void updatePdi(List<String> pdi) {
        this.pdi = pdi.stream().map(img -> new ProductImageEntity(img, this)).toList();

    }

    public void decreaseStock(Long orderCount) {
        long stock = this.stock - orderCount;

        if (stock <= 0) {
            if (ProductState.SALE.equals(this.productState)) {
                this.productState = ProductState.OUTOFSTOCK;
            }
        }
        this.stock = stock;
    }

    public void increaseStock(Long orderCount) {
        long stock = this.stock + orderCount;
        if (stock > 0) {
            if (ProductState.OUTOFSTOCK.equals(this.productState)) {
                this.productState = ProductState.SALE;

            }
        }
        this.stock = stock;

    }

    public void updateProduct(NaverChannelProductsDto productsDto) {
        this.productName = productsDto.getName();
        this.price = productsDto.getSalePrice();
        this.stock = productsDto.getStockQuantity();
        this.discountRate = productsDto.getDiscountedPrice();
        this.productState = productsDto.getStatusType();

    }

    public void updateProductState(ProductState productState) {
        this.productState = productState;
    }

}
