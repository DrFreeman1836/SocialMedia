package main.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostDto;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

  private final PostService postService;

  @GetMapping("/{id}/posts")
  public ResponseEntity<Map> viewPostByUser(@PathVariable Long id,
      @RequestParam Integer limit, @RequestParam Integer offset) {
    try {
      List<PostDto> listPosts = postService.findPostsByUser(id);
      return ResponseEntity.ok(Map.of("result", true, "posts", listPosts.stream().skip(offset).limit(limit)));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

}
