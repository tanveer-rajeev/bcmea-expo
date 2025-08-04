package com.betafore.evoting.ParticipantManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface ParticipantDao{
    Participant findById(Long id) throws CustomException;

    Participant save(Participant participant,Long organizerId,Long expoId) throws CustomException;

    List<Participant> all(Long expoId) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
