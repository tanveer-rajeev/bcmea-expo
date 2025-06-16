package com.betafore.evoting.ReportManagement;

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
@Table(name = "report")
public class Report extends BaseEntity {
    @Column(name = "expoId")
    private Long expoId;
    private Integer registeredGuest;
    private Integer hall;
    private Integer stall;
    private Integer question;
    private Integer managers;
    private Integer staff;
    private Integer vote;
    private Integer voteHallPavilion;
    private Integer gift;
    private Integer seminar;
    private Integer lottery;
    private Integer vip_guest;
}
