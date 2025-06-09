package com.h5radar.account.domain.account_user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.h5radar.account.domain.AbstractRepositoryTests;

class AccountUserRepositoryTests extends AbstractRepositoryTests {

  @Autowired
  private AccountUserRepository accountUserRepository;

  @Test
  void shouldSaveRadarUserWithAllFields() {
    final AccountUser technology = new AccountUser();
    technology.setSub("My sub");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    accountUserRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    Assertions.assertNotNull(technology.getCreatedBy());
    Assertions.assertNotNull(technology.getCreatedDate());
    Assertions.assertNotNull(technology.getLastModifiedBy());
    Assertions.assertNotNull(technology.getLastModifiedDate());
  }

  @Test
  void shouldFindSavedRadarUserById() {
    final AccountUser technology = new AccountUser();
    technology.setSub("MY");
    technology.setUsername("Very good username for RadarUser");

    Assertions.assertNull(technology.getId());
    accountUserRepository.saveAndFlush(technology);
    Assertions.assertNotNull(technology.getId());
    var id = technology.getId();

    Assertions.assertTrue(accountUserRepository.findById(id).isPresent());
  }

  @Test
  void shouldFailOnNullSub() {
    final AccountUser technology = new AccountUser();
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptySub() {
    final AccountUser technology = new AccountUser();
    technology.setSub("");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 255"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceSub() {
    final AccountUser technology = new AccountUser();
    technology.setSub(" ");
    technology.setUsername("My username");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "sub");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailOnNullUsername() {
    final AccountUser technology = new AccountUser();
    technology.setSub("My sub");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 1);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertEquals(constraintViolation.getMessage(), "must not be blank");
    }
  }

  @Test
  void shouldFailOnEmptyUsername() {
    final AccountUser technology = new AccountUser();
    technology.setSub("My sub");
    technology.setUsername("");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("size must be between 1 and 255"));
    }
  }

  @Test
  void shouldFailOnWhiteSpaceUsername() {
    final AccountUser technology = new AccountUser();
    technology.setSub("My sub");
    technology.setUsername(" ");

    Assertions.assertNull(technology.getId());
    ConstraintViolationException exception =
        catchThrowableOfType(() -> accountUserRepository.saveAndFlush(technology),
            ConstraintViolationException.class);

    Assertions.assertNotNull(exception);
    Assertions.assertEquals(exception.getConstraintViolations().size(), 2);
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      Assertions.assertEquals(constraintViolation.getPropertyPath().toString(), "username");
      Assertions.assertTrue(constraintViolation.getMessage().equals("must not be blank")
          || constraintViolation.getMessage().equals("should be without whitespaces before and after"));
    }
  }

  @Test
  void shouldFailToSaveRadarUserDueToSubWithRightWhiteSpace() {
    final AccountUser technology = new AccountUser();
    technology.setSub("My new test RadarUser ");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> accountUserRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldFailToSaveRadarUserDueToSubWithLeftWhiteSpace() {
    final AccountUser technology = new AccountUser();
    technology.setSub(" My new test RadarUser");

    Assertions.assertNull(technology.getId());
    assertThatThrownBy(() -> accountUserRepository.saveAndFlush(technology))
        .isInstanceOf(ValidationException.class);
  }
}
