package com.betafore.evoting.GalaEventManagement;

import com.betafore.evoting.entities.BaseEntity;
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
@Table(name = "gala_event")
public class GalaEvent extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "img")
    private String img;

    @Column(name = "time_duration")
    private Integer duration;

    @Column(name = "num_of_guest")
    private Integer numOfGuest;

    @Column(name = "time")
    private String time;

    @Column(name = "capacity")
    private Integer capacity;

}
