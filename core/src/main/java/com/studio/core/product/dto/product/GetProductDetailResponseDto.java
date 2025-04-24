package com.studio.core.product.dto.product;



import com.querydsl.core.annotations.QueryProjection;
import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.global.response.ChannelDetailDto;
import com.studio.core.global.utils.CalculatorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "상품 상세 정보")
public class GetProductDetailResponseDto {

    @Schema(description = "상품 생성 번호")
    private Long id;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private Long price;


    @Schema(description = "할인율")
    private Long discountRate;

    @Schema(description = "재고 수량")
    private Long stock;

    @Schema(description = "상품 상태 enum")
    private ProductState productState;


    @Schema(description = "상품 판매 채널")
    private Channels channel;

    @Schema(description = "상품 판매 채널 키값")
    private String channelProductId;


    @Schema(description = "상품 대표 이미지 (Product Representative Image)")
    private String pri;


    @Builder.Default
    @Schema(description = "상품 상세 이미지 (Product Detail Image)")
    private List<String> pdi = new ArrayList<>();;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "매장 정보 등록 시간")
    private LocalDateTime createAt;

    @Schema(description = "매장 정보 수정 시간")
    private LocalDateTime updateAt;

    @QueryProjection
    public GetProductDetailResponseDto(Long id, String productName, Long price, Long discountRate, Long stock, ProductState productState, Channels channel, String channelProductId, String pri, List<String> pdi, String description, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.discountRate = discountRate;
        this.stock = stock;
        this.productState = productState;
        this.channel = channel;
        this.channelProductId = channelProductId;
        this.pri = pri;
        this.pdi = pdi;
        this.description = description;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @Schema(description = "상품 상태")
    public String getProductStateName() {
        return productState.getMeaning();
    }

    @Schema(description = "상품 판매 채널 상세정보")
    public ChannelDetailDto getChannelDetail() {

        return Channels.getChannelDetail(channel, id, channelProductId);

    }


    @Schema(description = "할인가")
    public Long getDiscountPrice() {
        return CalculatorUtil.calculateDiscountPrice(discountRate, price);
    }
}

