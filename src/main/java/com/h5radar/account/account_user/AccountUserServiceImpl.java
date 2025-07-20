package com.h5radar.account.account_user;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h5radar.account.domain.ModelError;
import com.h5radar.account.domain.ValidationException;

@RequiredArgsConstructor
@Service
@Transactional
public class AccountUserServiceImpl implements AccountUserService {

  private final Validator validator;
  private final AccountUserRepository accountUserRepository;
  private final AccountUserMapper accountUserMapper;

  @Override
  @Transactional(readOnly = true)
  public Collection<AccountUserDto> findAll() {
    return accountUserRepository.findAll(Sort.by(Sort.Direction.ASC, "sub"))
        .stream().map(accountUserMapper::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AccountUserDto> findAll(AccountUserFilter accountUserFilter, Pageable pageable) {
    return accountUserRepository.findAll((root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();
      if (accountUserFilter != null && accountUserFilter.getSub() != null
          && !accountUserFilter.getSub().isBlank()) {
        predicateList.add(builder.like(root.get("sub"), accountUserFilter.getSub()));
      }
      if (accountUserFilter != null && accountUserFilter.getUsername() != null
          && !accountUserFilter.getUsername().isBlank()) {
        predicateList.add(builder.like(root.get("username"), accountUserFilter.getUsername()));
      }
      return builder.and(predicateList.toArray(new Predicate[] {}));
    }, pageable).map(accountUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<AccountUserDto> findById(Long id) {
    return accountUserRepository.findById(id).map(accountUserMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<AccountUserDto> findBySub(String sub) {
    return accountUserRepository.findBySub(sub).map(accountUserMapper::toDto);
  }

  @Override
  @Transactional
  public AccountUserDto save(AccountUserDto accountUserDto) {
    AccountUser accountUser = accountUserMapper.toEntity(accountUserDto);
    // Throw exception if violations are exists
    List<ModelError> modelErrorList = new LinkedList<>();
    Set<ConstraintViolation<AccountUser>> constraintViolationSet = validator.validate(accountUser);
    if (!constraintViolationSet.isEmpty()) {
      for (ConstraintViolation<AccountUser> constraintViolation : constraintViolationSet) {
        modelErrorList.add(new ModelError(constraintViolation.getMessageTemplate(), constraintViolation.getMessage(),
            constraintViolation.getPropertyPath().toString()));
      }
      String errorMessage = ValidationException.buildErrorMessage(modelErrorList);
      throw new ValidationException(errorMessage, modelErrorList);
    }
    return accountUserMapper.toDto(accountUserRepository.save(accountUser));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    accountUserRepository.deleteById(id);
  }
}
