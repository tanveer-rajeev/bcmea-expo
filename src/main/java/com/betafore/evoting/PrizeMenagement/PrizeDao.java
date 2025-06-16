package com.betafore.evoting.PrizeMenagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface PrizeDao  {
    List<PrizeResponse> all(Long expoId) throws CustomException;

    Prize findById(Long id) throws CustomException;

    Prize save(PrizeDto prizeDto,Long expoId) throws CustomException;

    void update(PrizeDto t,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
