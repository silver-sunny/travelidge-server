package com.studio.batch.product.processor;


import com.studio.batch.global.provider.RequestDateProvider;
import com.studio.core.global.naver.dto.NaverChannelProductsDto;
import com.studio.core.global.naver.dto.NaverProductContentsDto;
import com.studio.core.global.naver.dto.NaverProductsResponseDto;
import com.studio.core.global.naver.service.NaverProductApiService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverProductReader implements ItemReader<NaverChannelProductsDto> {


    private final NaverProductApiService naverProductService;

    private List<NaverProductContentsDto> naverDataList = new ArrayList<>();

    private int nextDataIndex = 0;
    private int currentPage = 1;
    private boolean lastPage = false;

    private final RequestDateProvider requestDateProvider;


    @Value("${requestDate:#{null}}")
    private String requestDate;

    @Override
    public NaverChannelProductsDto read() {
        if (naverDataList.isEmpty() || nextDataIndex >= naverDataList.size()) {
            if (lastPage) {
                return null; // 마지막 페이지이면 더 이상 읽을 데이터 없음
            }
            fetchNextPage();
            nextDataIndex = 0;
        }

        return naverDataList.isEmpty() ? null
            : naverDataList.get(nextDataIndex++).getChannelProducts().get(0);

    }


    private void fetchNextPage() {
        NaverProductsResponseDto response = naverProductService.getNaverProducts(requestDate,
            currentPage);

        if (response != null) {
            this.naverDataList = response.getContents();
            this.lastPage = response.isLast();
            this.currentPage++; // 다음 페이지 요청을 위해 증가
        }
    }

}


