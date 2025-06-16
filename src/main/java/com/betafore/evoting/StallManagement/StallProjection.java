package com.betafore.evoting.StallManagement;

import org.springframework.beans.factory.annotation.Value;

public interface StallProjection {

    String getName();
    String getSlug();

    @Value("#{target.getLogo() != null ? \"/stalls/\" + target.getSlug() + \"/logo\" : null}")
    String getLogo();

    @Value("#{target.getBackground() != null ? \"/stalls/\" + target.getSlug() + \"/background\" : null}")
    String getBackground();

}
