package com.h5radar.account.account_user;

import org.mapstruct.Mapper;

import com.h5radar.account.PlainMapper;
import com.h5radar.account.config.MapperConfiguration;

@Mapper(config = MapperConfiguration.class)
public interface AccountUserMapper extends PlainMapper<AccountUser, AccountUserDto> {
}
