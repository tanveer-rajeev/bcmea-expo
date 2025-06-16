package com.betafore.evoting.SeminarManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface SeminarDao {
    List<Seminar> all(Long expoId) throws CustomException;

    Seminar findById(Long id) throws CustomException;

    Seminar save(SeminarDto seminarDto,Long expoId) throws CustomException;

    Seminar saveAndFlush(Seminar t,Long id);

    void removeById(Long id) throws CustomException;

    void removeAll(Long epoId) throws CustomException;
}
