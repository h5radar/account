package com.h5radar.account.account_user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import com.h5radar.account.domain.AbstractIntegrationTests;


class AccountUserIntegrationTests extends AbstractIntegrationTests {

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetAccountUsers() {
    // Create accountUser
    AccountUserDto accountUserDto = new AccountUserDto();
    accountUserDto.setId(null);
    accountUserDto.setSub("My sub");
    accountUserDto.setUsername("My username");
    accountUserDto = accountUserService.save(accountUserDto);

    webTestClient.get().uri("/api/v1/account-users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.content").isArray()
        .jsonPath("$.content[0].id").isEqualTo(accountUserDto.getId())
        .jsonPath("$.content[0].sub").isEqualTo(accountUserDto.getSub())
        .jsonPath("$.content[0].username").isEqualTo(accountUserDto.getUsername());

    accountUserService.deleteById(accountUserDto.getId());
  }

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetAccountUser() {
    // Create accountUser
    AccountUserDto accountUserDto = new AccountUserDto();
    accountUserDto.setId(null);
    accountUserDto.setSub("My sub");
    accountUserDto.setUsername("My username");
    accountUserDto = accountUserService.save(accountUserDto);

    webTestClient.get().uri("/api/v1/account-users/{id}", accountUserDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$").isNotEmpty()
        .jsonPath("$").isMap()
        .jsonPath("$.id").isEqualTo(accountUserDto.getId())
        .jsonPath("$.sub").isEqualTo(accountUserDto.getSub())
        .jsonPath("$.username").isEqualTo(accountUserDto.getUsername());

    accountUserService.deleteById(accountUserDto.getId());
  }
}
