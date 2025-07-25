package com.h5radar.account.account_user;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountUserFilter {

  @Size(min = 0, max = 255)
  private String sub;

  @Size(min = 0, max = 255)
  private String username;

}
