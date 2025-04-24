package com.studio.core.product.dto.product;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;

@Getter
public class ProductImage {

    private String url;

    public ProductImage(String url) {
        this.url = url;
    }


}
