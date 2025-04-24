package com.studio.api.product.service;


import com.studio.core.product.dto.product.ProductPopularSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductPopularSearchService popularSearchService;


    // 상품 검색 및 검색어 누적
//    public Page<ProductSearchResponseDto> searchProducts(String keyword, Pageable pageable) {
//        // 사용자가 입력한 검색어 자체에 대한 카운트 누적
//        popularSearchService.incrementSearchCount(keyword);
//
//        // 상품 검색
//        Page<ProductEntity> productEntities = productSearchQueryJpaRepository
//            .searchProducts(keyword, ProductState.SALE, Channels.TRAVELIDGE);
//
//        // 검색 결과로 나온 상품명들을 기준으로도 카운트 누적
//        productEntities.forEach(product -> {
//            String productName = product.getProductName();
//            if (!productName.equalsIgnoreCase(keyword)) {
//                popularSearchService.incrementSearchCount(productName);
//            }
//        });
//
//        // 검색 결과 상품명 추출
//        List<String> productNames = productEntities.stream()
//            .map(ProductEntity::getProductName)
//            .collect(Collectors.toList());
//
//        // Redis에서 상품명 기준으로 검색 수 가져오기
//        final List<Double> searchCounts = popularSearchService.getSearchCount(productNames);
//
//        // 검색 수 개수가 상품 개수와 맞지 않으면 기본값으로 처리
//        if (searchCounts.size() != productEntities.size()) {
//            return IntStream.range(0, productEntities.size())
//                .mapToObj(i -> ProductSearchResponseDto.from(
//                    productEntities.get(i),
//                    0L)) // 기본값 0으로 처리
//                .collect(Collectors.toList());
//        }
//
//        // 결과 DTO로 변환
//        return IntStream.range(0, productEntities.size())
//            .mapToObj(i -> ProductSearchResponseDto.from(
//                productEntities.get(i),
//                searchCounts.get(i) != null ? searchCounts.get(i).longValue() : 0L
//            ))
//            .collect(Collectors.toList());
//    }

    public List<ProductPopularSearchResponseDto> getTopSearchKeywords(int topN) {
        List<String> keywords = new ArrayList<>(popularSearchService.getTopSearchKeywords(topN));

        return IntStream.range(0, topN)
            .mapToObj(i -> {
                String keyword = (i < keywords.size()) ? keywords.get(i) : "-";
                return ProductPopularSearchResponseDto.from(keyword, i + 1);
            })
            .collect(Collectors.toList());
    }

    public void incrementSearchCount(String keyword) {
        popularSearchService.incrementSearchCount(keyword);
    }
}