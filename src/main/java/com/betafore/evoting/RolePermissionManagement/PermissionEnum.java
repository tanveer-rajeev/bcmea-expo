package com.betafore.evoting.RolePermissionManagement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum {

    ROLE_READ("role:read"),
    ROLE_UPDATE("role:update"),
    ROLE_CREATE("role:create"),
    ROLE_DELETE("role:delete"),

    SETTINGS_READ("settings:read"),
    SETTINGS_UPDATE("settings:update"),
    SETTINGS_CREATE("settings:create"),
    SETTINGS_DELETE("settings:delete"),

    ORGANIZER_READ("organizer:read"),
    ORGANIZER_UPDATE("organizer:update"),
    ORGANIZER_CREATE("organizer:create"),
    ORGANIZER_DELETE("organizer:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    // Others permissions
    EXPO_READ("expo:read"),
    EXPO_UPDATE("expo:update"),
    EXPO_CREATE("expo:create"),
    EXPO_DELETE("expo:delete"),

    PARTICIPANT_READ("participant:read"),
    PARTICIPANT_UPDATE("participant:update"),
    PARTICIPANT_CREATE("participant:create"),
    PARTICIPANT_DELETE("participant:delete"),

    VIP_READ("vip:read"),
    VIP_UPDATE("vip:update"),
    VIP_CREATE("vip:create"),
    VIP_DELETE("vip:delete"),

    HALL_READ("hall:read"),
    HALL_UPDATE("hall:update"),
    HALL_CREATE("hall:create"),
    HALL_DELETE("hall:delete"),

    STALL_READ("stall:read"),
    STALL_UPDATE("stall:update"),
    STALL_CREATE("stall:create"),
    STALL_DELETE("stall:delete"),

    QUESTION_READ("question:read"),
    QUESTION_UPDATE("question:update"),
    QUESTION_CREATE("question:create"),
    QUESTION_DELETE("question:delete"),

    GIFT_READ("gift:read"),
    GIFT_UPDATE("gift:update"),
    GIFT_CREATE("gift:create"),
    GIFT_DELETE("gift:delete"),

    SEMINAR_READ("seminar:read"),
    SEMINAR_UPDATE("seminar:update"),
    SEMINAR_CREATE("seminar:create"),
    SEMINAR_DELETE("seminar:delete"),

    LOTTERY_READ("lottery:read"),
    LOTTERY_UPDATE("lottery:update"),
    LOTTERY_CREATE("lottery:create"),
    LOTTERY_DELETE("lottery:delete"),

    REPORT_READ("report:read"),
    REPORT_UPDATE("report:update"),
    REPORT_CREATE("report:create"),
    REPORT_DELETE("report:delete"),

    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_DELETE("employee:delete"),

    PRIZE_CREATE("prize:create"),
    PRIZE_READ("prize:read"),
    PRIZE_UPDATE("prize:update"),
    PRIZE_DELETE("prize:delete"),

    GALA_CREATE("gala:create"),
    GALA_UPDATE("gala:update"),
    GALA_READ("gala:read"),
    GALA_DELETE("gala:delete"),

    VOTE_CREATE("vote:create"),
    VOTE_UPDATE("vote:update"),
    VOTE_READ("vote:read"),
    VOTE_DELETE("vote:delete");

    private final String permission;
}
