package com.studio.core.product.dto.product.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "기타 재화 상품정보제공고시")
public class NaverProductInfoEtcDto {

    @Schema(description = "제품하자/오배송에 따른 청약철회 조항", example = "0", defaultValue = "0")
    private String returnCostReason;

    @Schema(description = "제품하자가 아닌 소비자의 단순변심에 따른 청약철회가 불가능한 경우 그 구체적 사유와 근거", example = "0", defaultValue = "0")
    private String noRefundReason;

    @Schema(description = "재화 등의 교환ㆍ반품ㆍ보증 조건 및 품질 보증 기준", example = "0", defaultValue = "0")
    private String qualityAssuranceStandard;

    @Schema(description = "대금을 환불받기 위한 방법과 환불이 지연될 경우 지연배상금을 지급받을 수 있다는 사실 및 배상금 지급의 구체적인 조건·절차", example = "0", defaultValue = "0")
    private String compensationProcedure;

    @Schema(description = "소비자피해보상의 처리, 재화 등에 대한 불만 처리 및 소비자와 사업자 사이의 분쟁 처리에 관한 사항", example = "0", defaultValue = "0")
    private String troubleShootingContents;

    @Schema(description = "품명", example = "품명")
    private String itemName;

    @Schema(description = "모델명", example = "모델명")
    private String modelName;

    @Schema(description = "제조사", example = "제조사")
    private String manufacturer;

    @Schema(description = "AS 책임자", example = "as책임자")
    private String afterServiceDirector;


    public NaverProductInfoEtcDto(){
        this.returnCostReason = "0";
        this.noRefundReason = "0";
        this.qualityAssuranceStandard = "0";
        this.compensationProcedure="0";
        this.troubleShootingContents = "0";
        this.itemName = "0";
        this.modelName = "품명";;
        this.manufacturer= "제조사";
        this.afterServiceDirector = "as 책임자";
    }
}
