package com.h5radar.account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.h5radar.account.account_user.AccountUserDto;
import com.h5radar.account.account_user.AccountUserService;


@Component
public class AuthRequestInterceptor implements HandlerInterceptor {
  /*
   * Bearer constants from Authorization header
   */
  protected static final String BEARER = "Bearer ";
  /*
   * Account user sub constraint name
   */
  private static final String ACCOUNT_USERS_SUB_CONSTRAINTS = "uc_account_users_sub";


  @Autowired
  private AccountUserService accountUserService;


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    AccountUserDto accountUserDto = new AccountUserDto();
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith(BEARER)) {
      // Get payload section from jwt token
      String token = authHeader.substring(BEARER.length());
      String[] chunks = token.split("\\.");
      Base64.Decoder decoder = Base64.getUrlDecoder();
      String payload = new String(decoder.decode(chunks[1]));

      // Save accountUser and set id as attribute
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(payload);
      accountUserDto.setSub(rootNode.get("sub").asText());
      accountUserDto.setUsername(rootNode.get("preferred_username").asText());
    } else {
      // This code primary for tests (context is set based on @WithMockUser)
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication auth = securityContext.getAuthentication();
      accountUserDto.setSub(auth.getName());
      accountUserDto.setUsername(auth.getName());
    }

    try {
      accountUserDto = accountUserService.save(accountUserDto);
      Long accountUserId = accountUserDto == null ? 0 : accountUserDto.getId();
      request.setAttribute(AccountConstants.ACCOUNT_USER_ID_ATTRIBUTE_NAME, accountUserId);
    } catch (DataIntegrityViolationException exception) {
      if (exception.getMessage().toLowerCase().contains(ACCOUNT_USERS_SUB_CONSTRAINTS)) {
        Optional<AccountUserDto> accountUserDtoOptional =  accountUserService.findBySub(accountUserDto.getSub());
        if (accountUserDtoOptional.isPresent()) {
          request.setAttribute(AccountConstants.ACCOUNT_USER_ID_ATTRIBUTE_NAME, accountUserDtoOptional.get().getId());
          return true;
        }
      }
      throw exception;
    }

    // Continue processing the request
    return true;
  }
}
