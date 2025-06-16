package com.betafore.evoting.security_config;
import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.security_config.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token  {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id")
  public Organizer organizer;
}
