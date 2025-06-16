package com.betafore.evoting.RolePermissionManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    List<Role> findAllByExpoId(Long id);

    void deleteAllByExpoId(Long id);

    @Query("""
          select count (*) from Role r where r.name = "STAFF" and r.expoId =?1
        """)
    Integer totalStaff(Long expoId);

    @Query("""
          select count (*) from Role r where r.name = "MANAGER" and r.expoId =?1
        """)
    Integer totalManager(Long expoId);
}
