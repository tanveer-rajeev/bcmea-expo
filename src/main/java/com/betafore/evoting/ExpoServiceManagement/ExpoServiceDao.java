package com.betafore.evoting.ExpoServiceManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface ExpoServiceDao {
    List<ExpoService> all();

    ExpoService findById(Long id) throws CustomException;

    ExpoService save(ExpoService t) throws CustomException;

    void saveAndFlush(ExpoService t, Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll();
}
