package main.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {

  /**
   * Id пользователя
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Имя пользователя
   */
  @Column(nullable = false)
  private String name;

  /**
   * Пароль пользователя
   */
  @Column(nullable = false)
  private String password;

  /**
   * Email пользователя
   */
  @Column(nullable = false, unique = true)
  @Index(name = "indexEmail")
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Post> listPosts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Friends> listFriends;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Messages> listMessages;

}
