package com.betafore.evoting.VIP_GuestManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface VIP_GuestDao {

    List<VIP_Guest> all(Long expoId) throws CustomException;

    VIP_Guest findById(Long id) throws CustomException;

    VIP_Guest save(VIP_GuestDto t,Long participantId,Long expoId) throws CustomException;

    VIP_Guest saveAndFlush(VIP_Guest t,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
