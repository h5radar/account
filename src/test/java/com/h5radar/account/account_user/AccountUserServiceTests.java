package com.h5radar.account.account_user;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.account.AbstractServiceTests;
import com.h5radar.account.ValidationException;

class AccountUserServiceTests extends AbstractServiceTests {
  @MockitoBean
  private AccountUserRepository accountUserRepository;
  @Autowired
  private AccountUserMapper accountUserMapper;
  @Autowired
  private AccountUserService accountUserService;

  @Test
  void shouldFindAllTechnologies() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    List<AccountUser> technologyList = List.of(technology);
    Mockito.when(accountUserRepository.findAll(any(Sort.class))).thenReturn(technologyList);

    Collection<AccountUserDto> technologyDtoCollection = accountUserService.findAll();
    Assertions.assertEquals(1, technologyDtoCollection.size());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getSub(), technology.getSub());
    Assertions.assertEquals(technologyDtoCollection.iterator().next().getUsername(), technology.getUsername());
  }

  /*
  @Test
  // @Transactional
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
   */


  @Test
  void shouldFindAllTechnologiesWithEmptyFilter() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    List<AccountUser> technologyList = List.of(technology);
    Page<AccountUser> page = new PageImpl<>(technologyList);
    Mockito.when(accountUserRepository.findAll(ArgumentMatchers.<Specification<AccountUser>>any(), any(Pageable.class)))
        .thenReturn(page);

    AccountUserFilter technologyFilter = new AccountUserFilter();
    Pageable pageable = PageRequest.of(0, 10, Sort.by("sub,asc"));
    Page<AccountUserDto> technologyDtoPage = accountUserService.findAll(technologyFilter, pageable);
    // Mockito.verify(accountUserRepository).findAll(
    //     Specification.allOf((root, query, criteriaBuilder) -> null), pageable);

    Assertions.assertEquals(1, technologyDtoPage.getSize());
    Assertions.assertEquals(0, technologyDtoPage.getNumber());
    Assertions.assertEquals(1, technologyDtoPage.getTotalPages());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getId(), technology.getId());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getSub(), technology.getSub());
    Assertions.assertEquals(technologyDtoPage.iterator().next().getUsername(), technology.getUsername());

  }

  /* TODO:
  @Test
  // @Transactional
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
  // @Transactional
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

   */

  @Test
  void shouldFindByIdAccountUser() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(accountUserRepository.findById(technology.getId())).thenReturn(Optional.of(technology));
    Optional<AccountUserDto> technologyDtoOptional = accountUserService.findById(technology.getId());
    Mockito.verify(accountUserRepository).findById(technology.getId());

    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getSub(), technologyDtoOptional.get().getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDtoOptional.get().getUsername());

  }

  @Test
  void shouldfindBySubAccountUser() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(accountUserRepository.findBySub(technology.getSub())).thenReturn(Optional.of(technology));
    Optional<AccountUserDto> technologyDtoOptional = accountUserService.findBySub(technology.getSub());
    Mockito.verify(accountUserRepository).findBySub(technology.getSub());

    Assertions.assertTrue(technologyDtoOptional.isPresent());
    Assertions.assertEquals(technology.getId(), technologyDtoOptional.get().getId());
    Assertions.assertEquals(technology.getSub(), technologyDtoOptional.get().getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDtoOptional.get().getUsername());

  }

  @Test
  void shouldSaveAccountUser() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.when(accountUserRepository.save(any())).thenReturn(technology);
    AccountUserDto technologyDto = accountUserService.save(accountUserMapper.toDto(technology));
    Mockito.verify(accountUserRepository).save(any());

    Assertions.assertEquals(technology.getId(), technologyDto.getId());
    Assertions.assertEquals(technology.getSub(), technologyDto.getSub());
    Assertions.assertEquals(technology.getUsername(), technologyDto.getUsername());

  }

  @Test
  void shouldFailToSaveAccountUserDueToTitleWithWhiteSpace() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub(" My sub ");
    technology.setUsername("My username");

    ValidationException exception = catchThrowableOfType(() ->
        accountUserService.save(accountUserMapper.toDto(technology)), ValidationException.class);
    Assertions.assertFalse(exception.getMessage().isEmpty());
    Assertions.assertTrue(exception.getMessage().contains("should be without whitespaces before and after"));
  }

  @Test
  void shouldDeleteAccountUser() {
    final AccountUser technology = new AccountUser();
    technology.setId(10L);
    technology.setSub("My sub");
    technology.setUsername("My username");

    Mockito.doAnswer((i) -> null).when(accountUserRepository).deleteById(technology.getId());
    accountUserService.deleteById(technology.getId());
    Mockito.verify(accountUserRepository).deleteById(technology.getId());
  }
}
