package com.h5radar.account.account_user;

import jakarta.validation.Valid;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h5radar.account.AccountConstants;

@RestController
@Tag(name = "AccountUser API")
@RequestMapping("/api/v1/account-users")
@RequiredArgsConstructor
public class AccountUserController {

  private final AccountUserService accountUserService;

  @GetMapping("")
  public ResponseEntity<Page<AccountUserDto>> index(
      @RequestAttribute(AccountConstants.ACCOUNT_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @Valid AccountUserFilter accountUserFilter,
      @RequestParam(defaultValue = "${application.paging.page}") int page,
      @RequestParam(defaultValue = "${application.paging.size}") int size,
      @RequestParam(defaultValue = "sub,asc") String[] sort) {

    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);
    Page<AccountUserDto> accountUserDtoPage =
        accountUserService.findAll(accountUserFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    return ResponseEntity.status(HttpStatus.OK).body(accountUserDtoPage);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<AccountUserDto> show(
      @RequestAttribute(AccountConstants.ACCOUNT_USER_ID_ATTRIBUTE_NAME) Long radarUserId,
      @PathVariable("id") Long id) {
    Optional<AccountUserDto> accountUserRecord = accountUserService.findById(radarUserId);
    if (accountUserRecord.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(accountUserRecord.get());
  }

}
