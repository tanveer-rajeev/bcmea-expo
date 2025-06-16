package com.betafore.evoting.ExpoManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface ExpoDao {
    List<Expo> all();

    Expo findById(Long id) throws CustomException;

    Expo save(ExpoDto t) throws CustomException;

    void saveAndFlush(ExpoDto t,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

}
