package com.betafore.evoting.WinnerManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;
import java.util.Set;

public interface WinnerDao {
    Set<Winner> all(Long lotteryId,Long expoId) throws CustomException;

    Winner save(WinnerDto winner,Long expoId) throws CustomException;

    Winner saveAndFlush(WinnerDto t,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
