package main.repository;

import java.util.List;
import java.util.Optional;
import main.model.Friends;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepo extends JpaRepository<Friends, Long> {

  Optional<Friends> findByUserAndFrendUser(User user, User frendUser);

  @Query(value = "select * "
      + "from friends "
      + "where frend_user = :my_id "
      + "and status = 'ACCEPTED'", nativeQuery = true)
  List<Friends> findMyFriends(@Param("my_id") Long myId);

  @Query(value = "select * "
      + "from friends "
      + "where frend_user = :my_id "
      + "and status = 'ACCEPTED' or "
      + "status = 'DECLINED'", nativeQuery = true)
  List<Friends> findMySubscriptions(@Param("my_id") Long myId);

}
