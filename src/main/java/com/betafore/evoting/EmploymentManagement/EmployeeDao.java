package com.betafore.evoting.EmploymentManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface EmployeeDao {
    Employee findById(Long id) throws CustomException;

    Employee save(Employee employee, Long expoId, Long participantId) throws CustomException;

    List<Employee> allEmployeeByParticipant(Long participantId) throws CustomException;

    List<Employee> allByExpoId(Long expoId) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
