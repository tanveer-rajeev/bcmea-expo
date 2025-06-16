package com.betafore.evoting.UserManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        select u from User u where u.email = ?1
        """)
    Optional<User> findUserByEmail(String email);

    @Query("""
        select u from User u where u.phoneNumber = ?1
        """)
    Optional<User> findUserByPhoneNumber(String phoneNumber);

    @Query("""
        select u from User u where u.email = ?1 and u.phoneNumber = ?2 and u.expoId = ?3
        """)
    Optional<User> findByEmailAndPhoneNumberAndExpoId(String email, String phoneNumber, Long expoId);

    @Query("""
        select u from User u where u.email = ?1 and u.expoId = ?2
        """)
    Optional<User> findUserByEmailAndExpoId(String email, Long expoId);

    @Query("""
        select u from User u where u.phoneNumber = ?1 and u.expoId = ?2
        """)
    Optional<User> findUserByPhoneNumberAndExpoId(String phoneNumber, Long expoId);

    @Query("""
               select u from User u where u.isAttendExpo = true and u.expoId =?1
        """)
    List<User> findAllAttendedUser(Long expoId);

    @Query("""
          select count (*) from User u where u.isAttendExpo = true and u.expoId =?1
        """)
    Integer findTotalRegisteredUser(Long expoId);

    List<User> findAllByExpoId(Long expoId);


}
