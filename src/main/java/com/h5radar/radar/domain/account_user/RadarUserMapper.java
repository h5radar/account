package com.h5radar.radar.domain.account_user;

import org.mapstruct.Mapper;

import com.h5radar.radar.config.MapperConfiguration;
import com.h5radar.radar.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface RadarUserMapper extends PlainMapper<RadarUser, RadarUserDto> {
}
