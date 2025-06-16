package com.betafore.evoting.OrganizerManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query("""
        select o from Organizer o where o.email = ?1
        """)
    Optional<Organizer> findOrganizerByEmail(String email);

    Optional<Organizer> findByPhoneNumber(String phoneNumber);

    Optional<Organizer> findByEmailOrPhoneNumber(String email, String phoneNumber);

    @Query("""
        select o from Organizer o where o.phoneNumber = ?1 and o.expoId = ?2
        """)
    Optional<Organizer> findOrganizerByPhoneNumberAndExpoId(String phoneNumber, Long expoId);

    @Query("""
        select o from Organizer o where o.email = ?1 and o.expoId = ?2
        """)
    Optional<Organizer> findOrganizerByEmailAndExpoId(String email, Long expoId);

    Optional<Organizer> findByEmailAndExpoId(String email, Long expoId);

    List<Organizer> findAllByExpoId(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
