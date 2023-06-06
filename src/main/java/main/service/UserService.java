package main.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import main.dto.response.UserDto;
import main.exception.UserException;
import main.model.Friends;
import main.model.Status;
import main.model.User;
import main.repository.FriendRepo;
import main.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;

  private final AuthService authService;

  private final FriendRepo friendRepo;

  /**
   * Друзья пользователя
   * @return
   * @throws Exception
   */
  public List<UserDto> findMyFriends()  throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));

    return user.getListFriends().stream()
        .filter(f -> f.getStatus().equals(Status.ACCEPTED))
        .map(f -> {
          return UserDto.builder().id(f.getFrendUser().getId()).name(f.getFrendUser().getName())
              .build();
        }).toList();
  }

  /**
   * Подписки пользователя
   * @return
   * @throws Exception
   */
  public Set<UserDto> findMySubscriptions()  throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));
    return new HashSet<>(user.getListFriends().stream()
        .map(f -> {
          return UserDto.builder().id(f.getFrendUser().getId()).name(f.getFrendUser().getName())
              .build();
        }).toList());
  }

  public Set<User> getMySubscriptions(Long idUser)  throws Exception {
    User user = userRepo.findById(idUser).orElseThrow(() -> new UserException("Пользователь не найден"));
    return new HashSet<>(user.getListFriends().stream()
        .map(Friends::getFrendUser).toList());
  }

  /**
   * Создать заявку в друзья
   * @param idFriend id пользователя которому отправляется заявка
   * @throws Exception
   */
  public void requestOfFriends(Long idFriend)  throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));
    User userFriend = userRepo.findById(idFriend).orElseThrow(() -> new UserException("Пользователь не найден"));
    if (friendRepo.findByUserAndFrendUser(user, userFriend).isPresent()) {
      throw new UserException("Заявка уже отправлена");
    }
    Friends newFriend = new Friends();
    newFriend.setUser(user);
    newFriend.setFrendUser(userFriend);
    newFriend.setStatus(Status.DECLINED);
    friendRepo.save(newFriend);
  }

  /**
   * Подтвердить заявку в друзья
   * @param subscribeId id связи из таблицы FRIENDS
   * @throws Exception
   */
  public void acceptRequestOfFriends(Long subscribeId) throws Exception {
    Optional<Friends> friends = friendRepo.findById(subscribeId);
    if (friends.isEmpty()) {
      throw new UserException("Заявка не найдена");
    }
    Friends newFriend = new Friends();
    newFriend.setUser(friends.get().getFrendUser());
    newFriend.setFrendUser(friends.get().getUser());
    newFriend.setStatus(Status.ACCEPTED);
    friendRepo.save(newFriend);

    friends.get().setStatus(Status.ACCEPTED);
    friendRepo.flush();
  }

  /**
   * Удалить из друзей
   * @param friendId id пользователя которого удаляют из друзей
   * @throws Exception
   */
  public void deleteFromFriends(Long friendId) throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));
    User userFriend = userRepo.findById(friendId).orElseThrow(() -> new UserException("Пользователь не найден"));
    Optional<Friends> friends = friendRepo.findByUserAndFrendUser(user, userFriend);

    Friends friends2 = friendRepo.findByUserAndFrendUser(friends.get().getFrendUser(), friends.get().getUser())
        .orElseThrow(() -> new UserException("Заявка не найдена"));
    friends2.setStatus(Status.DECLINED);
    friendRepo.save(friends2);

    friendRepo.delete(friends.get());
  }

}
