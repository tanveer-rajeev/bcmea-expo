package com.betafore.evoting.ReportManagement;


import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface ReportDao {

    ReportDto report(Long expoId) throws CustomException;

    Report save(Long expoId) throws CustomException;

    Report update(ReportDto reportDto, Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
