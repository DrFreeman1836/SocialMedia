package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostDto;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

  private final PostService postService;

  @Operation(summary = "Add post")
  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping
  public ResponseEntity<Map> addPost(@RequestBody PostDto postDto) {
    try {
      postService.addPost(postDto);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Update post by id")
  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping("/{id}")
  public ResponseEntity<Map> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
    try {
      postService.updatePost(id, postDto);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Delete post by id")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/{id}")
  public ResponseEntity<Map> updatePost(@PathVariable Long id) {
    try {
      postService.deletePost(id);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "get post feed")
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/feedPost")
  public ResponseEntity<Map> getFeedPost(@RequestParam Integer limit, @RequestParam Integer offset) {
    try {
      List<PostDto> listPosts = postService.getFeedPost();
      return ResponseEntity.ok(Map.of("result", true, "posts", listPosts.stream().skip(offset).limit(limit)));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

}
