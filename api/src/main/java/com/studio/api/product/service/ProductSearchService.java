package com.studio.api.product.service;


import com.studio.core.global.enums.Channels;
import com.studio.core.global.enums.ProductState;
import com.studio.core.product.dto.product.ProductPopularSearchResponseDto;
import com.studio.core.product.dto.product.ProductSearchResponseDto;
import com.studio.core.product.entity.ProductEntity;
import com.studio.core.product.repository.ProductSearchJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductPopularSearchService popularSearchService;
    private final ProductSearchJpaRepository productSearchJpaRepository;


    public Page<ProductSearchResponseDto> searchProducts(String keyword, Pageable pageable) {

        // 1. 사용자가 입력한 검색어 자체에 대한 카운트 누적
        popularSearchService.incrementSearchCountAsync(keyword);

        // 2. 상품 검색
        List<ProductEntity> products = productSearchJpaRepository
            .findByProductNameContainingAndProductStateAndChannel(
                keyword,
                ProductState.SALE,
                Channels.TRAVELIDGE
            );

        // 3. 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductEntity> pagedProducts = products.subList(start, end);

        // 4. 검색어 조회수 조회
        Long searchCount = popularSearchService.getSearchCount(List.of(keyword)).get(0).longValue();

        // 5. DTO 변환
        List<ProductSearchResponseDto> dtoList = pagedProducts.stream()
            .map(product -> ProductSearchResponseDto.from(product, searchCount))
            .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, products.size());
    }

    public List<ProductPopularSearchResponseDto> getTopSearchKeywords(int topN) {
        List<String> keywords = new ArrayList<>(popularSearchService.getTopSearchKeywords(topN));

        return IntStream.range(0, topN)
            .mapToObj(i -> {
                String word = (i < keywords.size()) ? keywords.get(i) : "-";
                return ProductPopularSearchResponseDto.from(word, i + 1);
            })
            .collect(Collectors.toList());
    }


    public void incrementSearchCount(String keyword) {
        popularSearchService.incrementSearchCount(keyword);
    }
}
