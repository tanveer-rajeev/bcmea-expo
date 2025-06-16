package com.betafore.evoting.ExpoManagement;

import com.betafore.evoting.ExpoServiceManagement.ExpoService;
import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expo")
public class Expo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String expoName;
    private String status;
    private String clientName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "expo_service_join", joinColumns = @JoinColumn(name = "expoId"),
        inverseJoinColumns = @JoinColumn(name = "serviceId"))
    private Set<ExpoService> expoServiceSet;

}
