package com.betafore.evoting.HallManagement;

import com.betafore.evoting.StallManagement.StallResource;
import lombok.Data;

import java.util.List;

@Data
public class HallResource {

    private final Long id;
    private final String name;
    private final String slug;
    private final List<StallResource> stalls;

}
