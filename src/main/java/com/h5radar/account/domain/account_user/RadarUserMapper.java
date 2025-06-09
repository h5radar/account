package com.h5radar.account.domain.account_user;

import org.mapstruct.Mapper;

import com.h5radar.account.config.MapperConfiguration;
import com.h5radar.account.domain.PlainMapper;

@Mapper(config = MapperConfiguration.class)
public interface RadarUserMapper extends PlainMapper<RadarUser, RadarUserDto> {
}
