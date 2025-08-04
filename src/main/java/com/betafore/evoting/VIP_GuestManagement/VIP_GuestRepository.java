package com.betafore.evoting.VIP_GuestManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VIP_GuestRepository extends JpaRepository<VIP_Guest,Long> {
    @Query("""
          select v from VIP_Guest v where v.expoId =?1
        """)
    List<VIP_Guest> findAllByExpoId(Long expoId);

    @Query("""
        select v from VIP_Guest  v where v.id =?1
        """)
    Optional<VIP_Guest> findVIP_GuestById(Long id);

    @Query("""
        select v from VIP_Guest  v where v.email =?1 and v.expoId  =?2
        """)
    Optional<VIP_Guest> findVIP_GuestByEmailAndExpoId(String email,Long expoId);

    @Query("""
        select v from VIP_Guest  v where v.phoneNumber =?1 and v.expoId  =?2
        """)
    Optional<VIP_Guest> findVIP_GuestByPhoneNumberAndExpoId(String phoneNumber,Long expoId);

    @Query("""
        select v from VIP_Guest  v where v.phoneNumber =?1 or v.email = ?2
        """)
    Optional<VIP_Guest> findVIP_GuestByPhoneNumberOrEmail(String phoneNumber,String email);

    @Query("""
        select v from VIP_Guest  v where v.expoId =?1 and (v.phoneNumber =?2 or v.email = ?3)
        """)
    Optional<VIP_Guest> findVIP_GuestByExpoIdAndPhoneNumberOrEmail(Long expoId,String phoneNumber,String email);

    @Query("""
          select count (*) from VIP_Guest v where v.expoId =?1
        """)
    Integer totalVIP_Guest(Long expoId);

    void deleteAllByExpoId(Long expoId);
    void deleteAllByParticipantId(Long participantId);
}
