package com.betafore.evoting.dao;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface Dao<T> {

    List<T> all(Long expoId) throws CustomException;

   T findById(Long id) throws CustomException;

    T save(T t,Long expoId) throws CustomException;

    T saveAndFlush(T t,Long id);

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;

}
