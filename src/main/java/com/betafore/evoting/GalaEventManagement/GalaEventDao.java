package com.betafore.evoting.GalaEventManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface GalaEventDao {
    List<GalaEvent> all(Long expoId) throws CustomException;

    GalaEvent findById(Long id) throws CustomException;

    GalaEvent save(GalaEventDto seminarDto,Long expoId) throws CustomException;

    GalaEvent saveAndFlush(GalaEvent t,Long id);

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId);
}
