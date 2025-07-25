package com.h5radar.account.account_user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import com.h5radar.account.AbstractAuditable;

@Entity
@Table(name = "account_users")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountUser extends AbstractAuditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 255)
  @AccountUserTrimSubConstraint
  @Column(name = "sub", unique = true, nullable = false)
  private String sub;

  @NotBlank
  @Size(min = 1, max = 255)
  @AccountUserTrimUsernameConstraint
  @Column(name = "username", unique = true, nullable = false)
  private String username;
}
