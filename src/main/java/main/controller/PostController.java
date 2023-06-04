package main.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostDto;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<Map> addPost(@RequestBody PostDto postDto) {
    try {
      postService.addPost(postDto);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
    try {
      postService.updatePost(id, postDto);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map> updatePost(@PathVariable Long id) {
    try {
      postService.deletePost(id);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

}
