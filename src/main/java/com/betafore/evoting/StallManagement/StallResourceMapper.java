package com.betafore.evoting.StallManagement;

import com.betafore.evoting.mappers.Mapper;

import java.util.List;

public class StallResourceMapper implements Mapper<StallResource, Stall> {

    @Override
    public List<StallResource> collection(List<Stall> data) {
        return data.stream().map(stall -> new StallResource(
            stall.getId(),
            stall.getName(),
            stall.getSlug(),
            stall.getLogo() != null ? "api/v1/stalls" + "/logo/" + stall.getId() : null,
            stall.getBackground() != null ? "api/v1/stalls" + "/background/" + stall.getId() : null,
            stall.getHall().getId()
        )).toList();
    }

    @Override
    public StallResource single(Stall data) {
        return new StallResource(
            data.getId(),
            data.getName(),
            data.getSlug(),
            data.getLogo() != null ? "/api/v1/stalls" + "/logo/" + data.getId() : null,
            data.getBackground() != null ? "/api/v1/stalls" + "/background/" + data.getId() : null,
            data.getHall().getId()
        );
    }

}
