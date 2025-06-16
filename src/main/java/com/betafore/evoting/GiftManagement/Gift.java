package com.betafore.evoting.GiftManagement;

import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift")
public class Gift extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "eligibility")
    private Integer eligibility;

    @Column(name = "img")
    private String img = null;

    @Column(name = "isEligibleForAll")
    private boolean isEligibleForAll;

}
