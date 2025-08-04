package com.betafore.evoting.EmailService;

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
@Table(name = "email_settings")
public class EmailSettings extends BaseEntity {

    @Column(name = "expo_id)")
    private Long expoId;

    @Column(name = "email_profile_name")
    private String emailProfileName;

    @Column(name = "smtp_mail_server")
    private String smtpMailServer;

    @Column(name = "smtp_user_password")
    private String smtpUserPassword;

    @Column(name = "smtp_username")
    private String smtpUsername;

    @Column(name = "sslOrTls")
    private String sslOrTls;

    @Column(name = "smtpPort")
    private String smtpPort;

    @Column(name = "active")
    private boolean active;
}
