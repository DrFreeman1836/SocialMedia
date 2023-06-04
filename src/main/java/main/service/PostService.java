package main.service;

import java.util.List;
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

  public void addPost(PostDto postDto) throws Exception {
    User user = userRepo.findByEmail(String.valueOf(authService.getAuthInfo().getPrincipal()))
        .orElseThrow(() -> new UserException("Пользователь не найден"));
    Post newPost = new Post();
    newPost.setText(postDto.getText());
    newPost.setTitle(postDto.getTitle());
    newPost.setUser(user);
    postRepo.save(newPost);
  }

  public void updatePost(Long id, PostDto postDto) throws Exception {
    Post oldPost = postRepo.findById(id).orElseThrow(() -> new UserException("Пост не найден"));
    oldPost.setTitle(postDto.getTitle());
    oldPost.setText(postDto.getText());
    postRepo.flush();
  }

  public void deletePost(Long id) throws UserException {
    try {
      postRepo.deleteById(id);
    } catch (Exception e) {
      throw new UserException("Пост не найден");
    }
  }

  public List<PostDto> findPostsByUser(Long idUser) {
    User user = userRepo.findById(idUser).orElseThrow(() -> new UserException("Пользователь не найден"));
    return postRepo.findPostByUser(user).stream().map(post -> {
      return PostDto.builder().title(post.getTitle()).text(post.getText()).build();
    }).collect(Collectors.toList());
  }

}
