package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "MESSAGES")
@Getter
@Setter
public class Messages {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Тескт сообщения
   */
  @Column(nullable = false)
  private String text;

  /**
   * Автор сообщения
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_user_id", nullable = false)
  private User user;

  /**
   * Адресант сообщения
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_user_id", nullable = false)
  private User toUser;

  /**
   * Время сообщения
   */
  @Column(nullable = false)
  private Date date;

}
