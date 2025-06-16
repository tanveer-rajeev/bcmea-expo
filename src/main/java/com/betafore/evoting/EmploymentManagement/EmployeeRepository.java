package com.betafore.evoting.EmploymentManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
          select v from Employee v where v.expoId =?1
        """)
    List<Employee> findAllByExpoId(Long expoId);

    @Query("""
          select v from Employee v where v.phoneNumber=?1 and v.expoId =?2
        """)
    Optional<Employee> findByPhoneNumberAndExpoId(String phoneNumber, Long expoId);

    @Query("""
        select v from Employee  v where v.id =?1
        """)
    Optional<Employee> findEmployeeById(Long id);

    @Query("""
        select v from Employee  v where v.email =?1 and v.expoId = ?2
        """)
    Optional<Employee> findEmployeeByEmailAndExpoId(String email, Long expoId);

    @Query("""
        select v from Employee  v where v.phoneNumber =?1
        """)
    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);

    @Query("""
        select v from Employee  v where v.phoneNumber =?1 or v.email =?2
        """)
    Optional<Employee> findEmployeeByPhoneNumberOrEmail(String phoneNumber, String email);

    Optional<Employee> findByExpoIdAndPhoneNumberOrEmail(Long expoId, String phoneNumber, String email);

    void deleteAllByExpoId(Long expoId);

    void deleteAllByParticipantId(Long participantId);

}
