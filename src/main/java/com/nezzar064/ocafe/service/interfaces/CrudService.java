package com.nezzar064.ocafe.service.interfaces;

import java.util.List;

public interface CrudService <T, ID> {

    List<T> findAll();

    T findById(ID id);

    T save(T object);

    T edit(T object, ID id);

    void delete(ID id);
}
