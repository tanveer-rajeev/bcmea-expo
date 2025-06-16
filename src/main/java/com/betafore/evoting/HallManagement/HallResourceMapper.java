package com.betafore.evoting.HallManagement;

import com.betafore.evoting.StallManagement.StallResourceMapper;
import com.betafore.evoting.mappers.Mapper;

import java.util.List;

public class HallResourceMapper implements Mapper<HallResource, Hall> {

    @Override
    public List<HallResource> collection(List<Hall> data) {
        return data.stream().map(hall -> new HallResource(
            hall.getId(),
            hall.getName(),
            hall.getSlug(),
            new StallResourceMapper().collection(hall.getStalls())
        )).toList();
    }

    @Override
    public HallResource single(Hall data) {
        return new HallResource(
            data.getId(),
            data.getName(),
            data.getSlug(),
            new StallResourceMapper().collection(data.getStalls())
        );
    }

}
