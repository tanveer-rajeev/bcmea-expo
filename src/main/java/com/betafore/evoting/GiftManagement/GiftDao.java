package com.betafore.evoting.GiftManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface GiftDao {
    List<Gift> all(Long expoId) throws CustomException;

    Gift findById(Long id) throws CustomException;

    Gift save(GiftDto t,Long expoId) throws CustomException;

    Gift saveAndFlush(GiftDto t,Long id);

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
