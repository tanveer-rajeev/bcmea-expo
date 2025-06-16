package com.betafore.evoting.VIP_GuestManagement;

import com.betafore.evoting.ParticipantManagement.Participant;
import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VIP_Guest")
public class VIP_Guest extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column(name = "name")
    private String name;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "company")
    private String company;

    @Column(name = "profession")
    private String profession;

    @Column(name = "country")
    private String country;

    @Column(name = "image")
    private String img;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    public Participant participant;
}
