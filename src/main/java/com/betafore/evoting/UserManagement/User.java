package com.betafore.evoting.UserManagement;

import com.betafore.evoting.VoteMangement.Vote;
import com.betafore.evoting.entities.BaseEntity;
import com.betafore.evoting.GiftManagement.Gift;
import com.betafore.evoting.SeminarManagement.Seminar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

    @Column(name = "expoId")
    private Long expoId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "company")
    private String company;

    @Column(name = "country")
    private String country;

    @Column(name = "address")
    private String address;

    @Column(name = "profession")
    private String profession;

    @Column(name = "is_interested_in_seminar")
    private boolean isInterestedInSeminar;

    @Column(name = "isAttendExpo")
    private boolean isAttendExpo;

    @Column(name = "totalGift")
    private Integer totalGiftCount;

    @Column(name = "isInvolvedWithCeramic")
    private boolean isInvolvedWithCeramic;

    @Column(name = "designation")
    private String designation;

    @Column(name = "wordAddress")
    private String workAddress;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_seminar_join", joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "seminarId"))
    private Set<Seminar> seminars;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_gift_join", joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "giftId"))
    private Set<Gift> gifts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Vote> votes;


    public void addSeminarList(Set<Seminar> seminar) {

        seminars = new HashSet<>();

        seminars.addAll(seminar);
    }

    public void addGiftList(Set<Gift> gift) {

        gifts = new HashSet<>();

        gifts.addAll(gift);
    }

    public void removeGift(Gift gift){
        if(!gifts.isEmpty()){
            gifts.remove(gift);
        }
    }

}
