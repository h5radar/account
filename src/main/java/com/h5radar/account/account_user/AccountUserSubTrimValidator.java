package com.h5radar.account.domain.account_user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class AccountUserSubTrimValidator implements ConstraintValidator<AccountUserTrimSubConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.length() == value.trim().length();
  }
}
