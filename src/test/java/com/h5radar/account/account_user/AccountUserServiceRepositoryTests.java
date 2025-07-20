package com.h5radar.account.account_user;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.account.AbstractServiceTests;

class AccountUserServiceRepositoryTests extends AbstractServiceTests {
  @Autowired
  private AccountUserRepository accountUserRepository;
  @Autowired
  private AccountUserService accountUserService;

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithNullFilter() {
    List<AccountUser> technologyList = List.of(
        new AccountUser(null, "My sub", "My username"),
        new AccountUser(null, "My new sub", "My new username"));
    for (AccountUser technology : technologyList) {
      accountUserRepository.save(technology);
    }

    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<AccountUserDto> technologyDtoPage = accountUserService.findAll(null, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithBlankSubFilter() {
    List<AccountUser> technologyList = List.of(
        new AccountUser(null, "My sub", "My username"),
        new AccountUser(null, "My new sub", "My new username"));
    for (AccountUser technology : technologyList) {
      accountUserRepository.save(technology);
    }

    AccountUserFilter accountUserFilter = new AccountUserFilter();
    accountUserFilter.setSub("");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<AccountUserDto> technologyDtoPage = accountUserService.findAll(accountUserFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(2, technologyDtoPage.getNumberOfElements());
  }

  @Test
  @Transactional
  void shouldFindAllTechnologiesWithTitleFilter() {
    List<AccountUser> technologyList = List.of(
        new AccountUser(null,  "My sub",  "My username"),
        new AccountUser(null, "My new sub",  "My new username"));
    for (AccountUser technology : technologyList) {
      accountUserRepository.save(technology);
    }

    AccountUserFilter accountUserFilter = new AccountUserFilter();
    accountUserFilter.setSub(technologyList.iterator().next().getSub());
    Pageable pageable = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "sub")));
    Page<AccountUserDto> technologyDtoPage = accountUserService.findAll(accountUserFilter, pageable);
    Assertions.assertEquals(10, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(1, technologyDtoPage.getNumberOfElements());
    Assertions.assertNotNull(technologyDtoPage.iterator().next().getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getSub(),
        technologyList.iterator().next().getSub());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getUsername(),
        technologyList.iterator().next().getUsername());
  }
}
