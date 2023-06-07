package main.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostDto;
import main.exception.UserException;
import main.model.Post;
import main.model.User;
import main.repository.PostRepo;
import main.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepo postRepo;

  private final UserRepo userRepo;

  private final AuthService authService;

  private final UserService userService;

  /**
   * Добавление поста
   * @param postDto
   * @throws Exception
   */
  public void addPost(PostDto postDto) throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не найден"));
    Post newPost = new Post();
    newPost.setText(postDto.getText());
    newPost.setTitle(postDto.getTitle());
    newPost.setUser(user);
    newPost.setDate(new Date());
    postRepo.save(newPost);
  }

  /**
   * Измненеие поста
   * @param id
   * @param postDto
   * @throws Exception
   */
  public void updatePost(Long id, PostDto postDto) throws Exception {
    Post oldPost = postRepo.findById(id).orElseThrow(() -> new UserException("Пост не найден"));
    oldPost.setTitle(postDto.getTitle());
    oldPost.setText(postDto.getText());
    postRepo.flush();
  }

  /**
   * Удаление поста
   * @param id
   * @throws UserException
   */
  public void deletePost(Long id) throws UserException {
    try {
      postRepo.deleteById(id);
    } catch (Exception e) {
      throw new UserException("Пост не найден");
    }
  }

  /**
   * Посты пользователя
   * @param idUser
   * @return
   * @throws Exception
   */
  public List<PostDto> findPostsByUser(Long idUser) throws Exception {
    User user = userRepo.findById(idUser).orElseThrow(() -> new UserException("Пользователь не найден"));
    return postRepo.findPostByUser(user).stream().map(post -> {
      return PostDto.builder().title(post.getTitle()).text(post.getText()).date(post.getDate()).build();
    }).collect(Collectors.toList());
  }

  public List<PostDto> getFeedPost() throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не определен"));
    Set<User> mySubscriptions = userService.getMySubscriptions(user.getId());
    List<PostDto> listFeedPost = new ArrayList<>();
    mySubscriptions.forEach(s -> {
      listFeedPost.addAll(s.getListPosts().stream().map(post -> {
        return PostDto.builder().title(post.getTitle()).text(post.getText()).date(post.getDate()).build();
      }).toList());
    });

    return listFeedPost.stream().sorted(Comparator.comparing(PostDto::getDate).reversed()).toList();
  }

}