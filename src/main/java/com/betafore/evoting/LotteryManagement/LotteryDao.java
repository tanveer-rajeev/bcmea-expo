package com.betafore.evoting.LotteryManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface LotteryDao {

    public void startLottery(Long lotteryId) throws CustomException;
    public void endLottery(Long lotteryId) throws CustomException;

    List<Lottery> all(Long expoId) throws CustomException;

    Lottery findById(Long id) throws CustomException;

    Lottery save(LotteryDto t,Long expoId) throws CustomException;

    void saveAndFlush(LotteryDto t,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
