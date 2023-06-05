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
    List<UserDto> listFriends = new ArrayList<>();
   listFriends.addAll(user.getListFriends().stream()
        .filter(f -> f.getStatus().equals(Status.ACCEPTED))
        .map(f -> {
          return UserDto.builder().id(f.getFrendUser().getId()).name(f.getFrendUser().getName())
              .build();
        }).toList());
    listFriends.addAll(friendRepo.findMyFriends(user.getId()).stream()
        .map(f -> {
          return UserDto.builder().id(f.getUser().getId()).name(f.getUser().getName()).build();
        }).toList());

    return listFriends;
  }

  /**
   * Подписки пользователя
   * @return
   * @throws Exception
   */
  public Set<UserDto> findMySubscriptions()  throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));
    Set<UserDto> listSubscriptions = new HashSet<>();
    listSubscriptions.addAll(user.getListFriends().stream()
        .map(f -> {
          return UserDto.builder().id(f.getFrendUser().getId()).name(f.getFrendUser().getName()).build();
        }).toList());
    listSubscriptions.addAll(friendRepo.findMySubscriptions(user.getId()).stream()
        .map(f -> {
          return UserDto.builder().id(f.getUser().getId()).name(f.getUser().getName()).build();
        }).toList());
    return listSubscriptions;
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
    friends.get().setStatus(Status.ACCEPTED);
    friendRepo.flush();
  }

  /**
   * Удалить из друзей
   * @param subscribeId id связи из таблицы FRIENDS
   * @throws Exception
   */
  public void deleteFromFriends(Long subscribeId) throws Exception {
    Optional<Friends> friends = friendRepo.findById(subscribeId);
    if (friends.isEmpty()) {
      throw new UserException("Заявка не найдена");
    }
    friends.get().setStatus(Status.DECLINED);
    friendRepo.flush();
  }

}
