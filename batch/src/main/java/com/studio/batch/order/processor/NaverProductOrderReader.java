package com.studio.batch.order.processor;

import com.studio.batch.global.provider.RequestDateProvider;
import com.studio.core.global.naver.dto.NaverDataDto;
import com.studio.core.global.naver.dto.NaverLastChangeStatusDto;
import com.studio.core.global.naver.dto.NaverOrderStatusDto;
import com.studio.core.global.naver.dto.NaverProductOrderIdsDto;
import com.studio.core.global.naver.dto.NaverProductOrderInfo;
import com.studio.core.global.naver.service.NaverOrderApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NaverProductOrderReader implements ItemReader<NaverDataDto> {

    private final NaverOrderApiService orderService;


    private List<NaverDataDto> naverDataDtos;
    private int nextDataIndex;


    private final RequestDateProvider requestDateProvider;

    @Override
    public NaverDataDto read() throws Exception {
        if (naverDataDtos == null) {
            naverDataDtos = fetchNaverProductOrders();
            nextDataIndex = 0;
        }

        NaverDataDto nextData = null;
        if (nextDataIndex < naverDataDtos.size()) {
            nextData = naverDataDtos.get(nextDataIndex);
            nextDataIndex++;
        }

        return nextData;
    }

    private List<NaverDataDto> fetchNaverProductOrders() {
        String requestDate = requestDateProvider.getFormattedRequestDateTime();
        NaverOrderStatusDto naverOrderStatus = orderService.getNaverProductOrder(requestDate);

        if (naverOrderStatus != null && naverOrderStatus.getData() != null && naverOrderStatus.getData().getCount() > 0) {
            List<String> orderIds = extractOrderIds(naverOrderStatus);
            NaverProductOrderInfo naverProductOrderInfo = orderService.getProductOrders(new NaverProductOrderIdsDto(orderIds));

            if (naverProductOrderInfo != null) {
                return naverProductOrderInfo.getData();
            }
        }

        return new ArrayList<>();
    }





    private List<String> extractOrderIds(NaverOrderStatusDto naverOrderStatus) {
        return naverOrderStatus.getData().getLastChangeStatuses()
                .stream()
                .map(NaverLastChangeStatusDto::getProductOrderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
