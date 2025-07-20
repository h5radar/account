package com.h5radar.account.account_user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long>,
    JpaSpecificationExecutor<AccountUser> {
  Optional<AccountUser> findBySub(String sub);
}
