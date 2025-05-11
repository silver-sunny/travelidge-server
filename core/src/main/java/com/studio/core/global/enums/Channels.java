package com.studio.core.global.enums;

import com.studio.core.global.response.ChannelDetailDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public enum Channels {
    ETC(0, "", "", "", null),
    TRAVELIDGE(1, "트레블릿지", "TRAVELIDGE", "https://travelidge.shop/product/",  true),
    NAVER(2, "네이버", "NAVER", "https://smartstore.naver.com/silver-sun/products/",  false);

    public final int index;
    public final String meaning;
    public final String channel;
    public final String url;
    public final Boolean isDefault;

    Channels(int index, String meaning, String channel, String url, Boolean isDefault) {
        this.index = index;
        this.meaning = meaning;
        this.channel = channel;
        this.url = url;
        this.isDefault = isDefault;
    }


    public static List<Map<String, Object>> getSalesChannelEnumList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Channels value : Channels.values()) {

            if (value == Channels.ETC) {
                continue;
            }

            Map<String, Object> channelMap = new HashMap<>();

            channelMap.put("index", value.index);
            channelMap.put("meaning", value.meaning);
            channelMap.put("channel", value.channel);
            channelMap.put("enumValue", value);
            channelMap.put("isDefault", value.isDefault);

            list.add(channelMap);
        }
        return list;
    }

    public static Channels getByIndex(Integer index) {
        for (Channels channel : Channels.values()) {
            if (channel.index == index) {
                return channel;
            }
        }
        return ETC;
    }


    public static ChannelDetailDto getChannelDetail(Channels channel, Long productId, String channelProductId) {

        ChannelDetailDto channelDetailDto = null;

        switch (channel ) {
            case TRAVELIDGE:
                // 자사는 앞에서 상품 등록이 완료된 상태여서 상품번호를 셋팅합니다.
                channelDetailDto = createChannelDetail(TRAVELIDGE, String.valueOf(productId));
                break;
            case NAVER:
                channelDetailDto = createChannelDetail(NAVER, channelProductId);
                break;
            default:
                channelDetailDto = createChannelDetail(ETC, null);

        }

        return channelDetailDto;
    }

    private static ChannelDetailDto createChannelDetail(Channels channel, String productNo) {
        return ChannelDetailDto.builder()
                .channelEnum(channel)
                .name(channel.meaning)
                .detailUrl(channel.url+productNo)
                .build();


    }
}
