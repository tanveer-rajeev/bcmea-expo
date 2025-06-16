package com.betafore.evoting.RolePermissionManagement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.betafore.evoting.RolePermissionManagement.PermissionEnum.*;

@Getter
@RequiredArgsConstructor
public enum PermissionGroup {

    ORGANIZER(
        Set.of(
            ORGANIZER_READ, ORGANIZER_UPDATE, ORGANIZER_DELETE, ORGANIZER_CREATE
        )
    ),
    ROLE(
        Set.of(
            ROLE_READ, ROLE_UPDATE, ROLE_DELETE, ROLE_CREATE
        )
    ),
    SETTINGS(
        Set.of(
            SETTINGS_READ, SETTINGS_UPDATE, SETTINGS_DELETE, SETTINGS_CREATE
        )
    ),
    USER(
        Set.of(
            USER_READ, USER_UPDATE, USER_DELETE, USER_CREATE
        )
    ),
    EXPO(
        Set.of(
            EXPO_READ, EXPO_UPDATE, EXPO_DELETE, EXPO_CREATE
        )
    ),
    PARTICIPANT(
        Set.of(
            PARTICIPANT_READ, PARTICIPANT_UPDATE, PARTICIPANT_DELETE, PARTICIPANT_CREATE
        )
    ),
    VIP(
        Set.of(
            VIP_READ, VIP_UPDATE, VIP_DELETE, VIP_CREATE
        )
    ),
    HALL(
        Set.of(
            HALL_READ, HALL_UPDATE, HALL_DELETE, HALL_CREATE
        )
    ),
    STALL(
        Set.of(
            STALL_READ, STALL_UPDATE, STALL_DELETE, STALL_CREATE
        )
    ),
    QUESTION(
        Set.of(
            QUESTION_READ, QUESTION_UPDATE, QUESTION_DELETE, QUESTION_CREATE
        )
    ),
    GIFT(
        Set.of(
            GIFT_READ, GIFT_UPDATE, GIFT_DELETE, GIFT_CREATE
        )
    ),
    SEMINAR(
        Set.of(
            SEMINAR_READ, SEMINAR_UPDATE, SEMINAR_DELETE, SEMINAR_CREATE
        )
    ),
    LOTTERY(
        Set.of(
            LOTTERY_READ, LOTTERY_UPDATE, LOTTERY_DELETE, LOTTERY_CREATE
        )
    ),
    REPORT(
        Set.of(
            REPORT_READ, REPORT_UPDATE, REPORT_DELETE, REPORT_CREATE
        )
    ),
    EMPLOYEE(
        Set.of(
            EMPLOYEE_READ, EMPLOYEE_UPDATE, EMPLOYEE_DELETE, EMPLOYEE_CREATE
        )
    ),
    PRIZE(
        Set.of(
            PRIZE_READ, PRIZE_UPDATE, PRIZE_DELETE, PRIZE_CREATE
        )
    ),
    GALA(
        Set.of(
            GALA_READ, GALA_UPDATE, GALA_DELETE, GALA_CREATE
        )
    ),
    VOTE(
        Set.of(VOTE_READ,VOTE_UPDATE,VOTE_DELETE,VOTE_CREATE)
    )
    ;


    private final Set<PermissionEnum> permissions;
}
