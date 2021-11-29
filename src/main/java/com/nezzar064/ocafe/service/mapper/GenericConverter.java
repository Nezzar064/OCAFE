package com.nezzar064.ocafe.service.mapper;

import java.util.List;

public interface GenericConverter<S, T> {

    T mapToEntity(S dto);

    S mapToDto(T entity);

    List<T> mapDtoListToEntityList(List<S> dtoList);

    List<S> mapEntityListToDtoList(List<T> entityList);
}
