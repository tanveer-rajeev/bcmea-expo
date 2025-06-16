package com.betafore.evoting.SmsConfig;

import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sms_settings")
public class SmsSettings extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column(name = "sms_request_url")
    private String smsRequestUrl;

    @Column(name = "sms_user_pass")
    private String smsToken;


}
