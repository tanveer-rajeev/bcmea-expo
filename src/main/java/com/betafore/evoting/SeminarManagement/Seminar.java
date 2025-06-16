package com.betafore.evoting.SeminarManagement;

import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seminar")
public class Seminar extends BaseEntity {

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
