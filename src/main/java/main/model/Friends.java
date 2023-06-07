package main.model;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FRIENDS")
@Getter
@Setter
public class Friends {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Пользователь
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * Друг пользолвателя
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "frend_user", nullable = false)
  private User frendUser;

  /**
   * Статус дружбы
   */
  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum('ACCEPTED', 'DECLINED') default 'DECLINED'", nullable = false)
  private Status status;

}
