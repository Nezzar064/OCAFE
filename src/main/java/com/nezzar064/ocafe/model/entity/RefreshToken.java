package com.nezzar064.ocafe.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refreshtoken_seq_gen")
  @SequenceGenerator(name = "refreshtoken_seq_gen", sequenceName = "refreshtoken_seq_gen", allocationSize = 1)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    RefreshToken that = (RefreshToken) o;

    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return 2003028305;
  }
}