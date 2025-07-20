package com.h5radar.account.account_user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountUserService {

  Collection<AccountUserDto> findAll();

  Page<AccountUserDto> findAll(AccountUserFilter accountUserFilter, Pageable pageable);

  Optional<AccountUserDto> findById(Long id);

  Optional<AccountUserDto> findBySub(String sub);

  AccountUserDto save(AccountUserDto accountUserDto);

  void deleteById(Long id);
}
