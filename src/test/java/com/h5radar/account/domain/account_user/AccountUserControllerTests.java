package com.h5radar.account.domain.account_user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.account.domain.AbstractControllerTests;

@WebMvcTest(AccountUserController.class)
public class AccountUserControllerTests extends AbstractControllerTests {

  @MockitoBean
  private AccountUserService accountUserService;

  @Test
  @WithMockUser
  public void shouldGetTechnologies() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

    Page<AccountUserDto> technologyDtoPage = new PageImpl<>(Arrays.asList(technologyDto));
    Mockito.when(accountUserService.findAll(any(), any())).thenReturn(technologyDtoPage);

    mockMvc.perform(get("/api/v1/account-users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content", hasSize(technologyDtoPage.getContent().size())))
        .andExpect(jsonPath("$.content[0].id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.content[0].sub", equalTo(technologyDto.getSub())))
        .andExpect(jsonPath("$.content[0].username", equalTo(technologyDto.getUsername())));

    Mockito.verify(accountUserService).findAll(any(), any());
  }

  public void shouldGetTechnologiesWithFilter() throws Exception {
    // TODO: get invalid it
  }

  public void shouldGetTechnologiesWithPaging() throws Exception {
    // TODO: get invalid it
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetTechnologiesDueToUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/account-users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser
  public void shouldGetRadarUser() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

    Mockito.when(accountUserService.findById(any())).thenReturn(Optional.of(technologyDto));

    mockMvc.perform(get("/api/v1/account-users/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(technologyDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(technologyDto.getUsername())));

    Mockito.verify(accountUserService).findById(technologyDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToGetRadarUserDueToUnauthorized() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);

    mockMvc.perform(get("/api/v1/account-users/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToGetRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldCreateRadarUser() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

    Mockito.when(accountUserService.save(any())).thenReturn(technologyDto);

    mockMvc.perform(post("/api/v1/account-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.id", equalTo(technologyDto.getId()), Long.class))
        .andExpect(jsonPath("$.sub", equalTo(technologyDto.getSub())))
        .andExpect(jsonPath("$.username", equalTo(technologyDto.getUsername())));

    Mockito.verify(accountUserService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToCreateRadarUserDueToUnauthorized() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);

    mockMvc.perform(post("/api/v1/account-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToCreateRadarUserDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToCreateRadarUserDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldUpdateRadarUser() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

    Mockito.when(accountUserService.findById(any())).thenReturn(Optional.of(technologyDto));
    Mockito.when(accountUserService.save(any())).thenReturn(technologyDto);

    mockMvc.perform(put("/api/v1/account-users/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isOk());

    Mockito.verify(accountUserService).findById(technologyDto.getId());
    Mockito.verify(accountUserService).save(any());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToUpdateRadarUserDueToUnauthorized() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);

    mockMvc.perform(put("/api/v1/account-users/{id}", technologyDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(technologyDto))
            .with(csrf()))
        .andExpect(status().isUnauthorized());

  }

  public void shouldFailToUpdateRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateRadarUserDueToEmptyTitle() throws Exception {
    // TODO: get invalid it
  }

  public void shouldFailToUpdateRadarUserDueToTitleWithSpaces() throws Exception {
    // TODO: get invalid it
  }


  @Test
  @WithMockUser
  public void shouldDeleteRadarUser() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);
    technologyDto.setSub("My sub");
    technologyDto.setUsername("My username");

    Mockito.when(accountUserService.findById(any())).thenReturn(Optional.of(technologyDto));
    Mockito.doAnswer((i) -> null).when(accountUserService).deleteById(any());

    mockMvc.perform(delete("/api/v1/account-users/{id}", technologyDto.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(accountUserService).findById(technologyDto.getId());
    Mockito.verify(accountUserService).deleteById(technologyDto.getId());
  }

  @Test
  @WithAnonymousUser
  public void shouldFailToDeleteRadarUserDueToUnauthorized() throws Exception {
    final AccountUserDto technologyDto = new AccountUserDto();
    technologyDto.setId(10L);

    mockMvc.perform(delete("/api/v1/account-users/{id}", technologyDto.getId())
            .with(csrf()))
        .andExpect(status().isUnauthorized());
  }

  public void shouldFailToDeleteRadarUserDueToInvalidId() throws Exception {
    // TODO: get invalid it
  }
}
