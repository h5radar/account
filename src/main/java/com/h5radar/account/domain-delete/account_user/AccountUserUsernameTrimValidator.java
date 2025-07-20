package com.h5radar.account.account_user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class AccountUserUsernameTrimValidator
    implements ConstraintValidator<AccountUserTrimUsernameConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
