package com.h5radar.account;

public interface PlainMapper<E, D> {

  D toDto(final E entity);

  E toEntity(final D dto);

}
