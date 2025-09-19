package com.h5radar.account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.h5radar.account.account_user.AccountUserDto;
import com.h5radar.account.account_user.AccountUserService;


@Component
public class AuthRequestInterceptor implements HandlerInterceptor {
  /*
   * Account user sub constraint name
   */
  private static final String ACCOUNT_USERS_SUB_CONSTRAINTS = "uc_account_users_sub";

  @Autowired
  private AccountUserService accountUserService;


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    AccountUserDto accountUserDto = new AccountUserDto();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
      throw new AccessDeniedException("JWT authentication required");
    }

    Jwt jwt = jwtAuth.getToken();
    accountUserDto.setSub(jwt.getClaimAsString("sub"));
    accountUserDto.setUsername(jwt.getClaimAsString("preferred_username"));

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
