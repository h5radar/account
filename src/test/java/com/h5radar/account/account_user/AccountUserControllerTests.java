package com.h5radar.account.account_user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.account.AbstractControllerTests;

@WebMvcTest(AccountUserController.class)
public class AccountUserControllerTests extends AbstractControllerTests {

  @Test
  public void shouldGetAccountUsers() throws Exception {
    final AccountUserDto accountUserDto = new AccountUserDto();
    accountUserDto.setId(10L);
    accountUserDto.setSub("My sub");
    accountUserDto.setUsername("My username");

    Mockito.when(accountUserService.save(any())).thenReturn(accountUserDto);
    Page<AccountUserDto> accountUserDtoPage = new PageImpl<>(Arrays.asList(accountUserDto));
    Mockito.when(accountUserService.findAll(any(), any())).thenReturn(accountUserDtoPage);

    mockMvc.perform(get("/api/v1/account-users")
            .with(jwt().jwt(j -> {
              j.claim("sub", accountUserDto.getSub());
              j.claim("preferred_username", accountUserDto.getUsername());
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(accountUserDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(accountUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].sub", equalTo(accountUserDto.getSub())))
        .andExpect(jsonPath("$.content[0].username", equalTo(accountUserDto.getUsername())));

    Mockito.verify(accountUserService).save(any());
    Mockito.verify(accountUserService).findAll(any(), any());
  }

  public void shouldGetAccountUsersWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetAccountUsersWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  public void shouldFailToGetAccountUsersDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/account-users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  public void shouldGetAccountUser() throws Exception {
    final AccountUserDto accountUserDto = new AccountUserDto();
    accountUserDto.setId(10L);
    accountUserDto.setSub("My sub");
    accountUserDto.setUsername("My username");

    Mockito.when(accountUserService.save(any())).thenReturn(accountUserDto);
    Mockito.when(accountUserService.findById(any())).thenReturn(Optional.of(accountUserDto));

    mockMvc.perform(get("/api/v1/account-users/{id}", accountUserDto.getId())
            .with(jwt().jwt(j -> {
              j.claim("sub", accountUserDto.getSub());
              j.claim("preferred_username", accountUserDto.getUsername());
            }))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(accountUserDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(accountUserDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(accountUserDto.getUsername())));

    Mockito.verify(accountUserService).save(any());
    Mockito.verify(accountUserService).findById(accountUserDto.getId());
  }

  @Test
  public void shouldFailToGetAccountUserDueToUnauthorized() throws Exception {
    final AccountUserDto accountUserDto = new AccountUserDto();
    accountUserDto.setId(10L);

    mockMvc.perform(get("/api/v1/account-users/{id}", accountUserDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetAccountUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
}
