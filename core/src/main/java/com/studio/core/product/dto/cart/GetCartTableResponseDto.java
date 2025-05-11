package com.studio.core.product.dto.cart;

import com.studio.core.global.enums.ProductState;
import com.studio.core.global.utils.CalculatorUtil;
import com.studio.core.global.utils.TimeCalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Schema(description = "장바구니")
public class GetCartTableResponseDto {

    @Schema(description = "장바구니 번호")
    private final Long id;

    @Schema(description = "상품 번호")
    private final Long productId;

    @Schema(description = "구매수량")
    private final int purchaseQuantity;

    @Schema(description = "옵션 직접입력 ( 예약날짜 )")
    private final String directOption;

    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private final String pri;

    @Schema(description = "상품 가격")
    private final Long price;

    @Schema(description = "할인율")
    private final Long discountRate;

    @Schema(description = "상품명")
    private final String productName;

    @Schema(description = "상품 상태")
    private final ProductState productState;

    @Schema(description = "장바구니 넣은 시간")
    private final String createAt;

    public GetCartTableResponseDto(Long id,Long productId, int purchaseQuantity, String directOption, LocalDateTime createAt, String pri, Long price, Long discountRate, String productName, ProductState productState) {
        this.id = id;
        this.productId = productId;
        this.purchaseQuantity = purchaseQuantity;
        this.directOption = directOption;
        this.createAt = TimeCalculatorUtil.getFormattedDate(createAt);
        this.pri = pri;
        this.price = price;
        this.discountRate = discountRate;
        this.productName = productName;
        this.productState = productState;
    }

    @Schema(description = "총가격")
    public Long getTotalPrice() {
        return price * purchaseQuantity;
    }


    @Schema(description = "할인된 총가격")
    public Long getDiscountTotalPrice() {
        return CalculatorUtil.calculateDiscountPrice(discountRate, price) * purchaseQuantity;
    }

}
