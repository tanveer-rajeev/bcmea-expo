package com.betafore.evoting.OrganizerManagement;

import com.betafore.evoting.dao.Dao;

import java.util.List;

public interface OrganizerServiceDao extends Dao<Organizer> {
    Organizer findById(Long id);

    Organizer save(Organizer organizer);

    List<Organizer> all();

    Organizer saveAndFlush(Organizer organizer,Long id);

    void removeById(Long id);

    void removeAll();
}
