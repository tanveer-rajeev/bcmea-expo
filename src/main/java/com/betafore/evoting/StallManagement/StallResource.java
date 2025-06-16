package com.betafore.evoting.StallManagement;

import lombok.Data;

import java.util.UUID;

@Data
public class StallResource {

    private final Long id;
    private final String name;
    private final String slug;
    private final String logo;
    private final String background;
    private final Long hallId;

}
