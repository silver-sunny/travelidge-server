package com.studio.core.global.enums.converter;

import com.studio.core.global.enums.Channels;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class ChannelsConverter implements AttributeConverter<Channels, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Channels channels) {
        return Optional.ofNullable(channels).map(Channels::getIndex).orElse(null);

    }

    @Override
    public Channels convertToEntityAttribute(Integer channelNo) {

       return Channels.getByIndex(channelNo);
    }
}
