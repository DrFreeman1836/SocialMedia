package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import main.dto.request.PostDto;
import main.dto.response.UserDto;
import main.service.PostService;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

  private final PostService postService;

  private final UserService userService;

  @Operation(summary = "Get posts by user id")
  @SecurityRequirement(name = "Bearer Authentication")
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

  @Operation(summary = "Get list my friends")
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/friends")
  public ResponseEntity<Map> getMyFriends() {
    try {
      List<UserDto> listFriends = userService.findMyFriends();
      return ResponseEntity.ok(Map.of("result", true, "friends", listFriends));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Get list my subscribe")
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/subscribe")
  public ResponseEntity<Map> getMySubscriptions() {
    try {
      Set<UserDto> listFriends = userService.findMySubscriptions();
      return ResponseEntity.ok(Map.of("result", true, "subscribe", listFriends));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Send friend request")
  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping("/friends/{id}")
  public ResponseEntity<Map> requestOfFriends(@PathVariable Long id) {
    try {
      userService.requestOfFriends(id);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Confirm friend request")
  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping("/subscribe/{id}")
  public ResponseEntity<Map> acceptRequestOfFriends(@PathVariable Long id) {
    try {
      userService.acceptRequestOfFriends(id);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

  @Operation(summary = "Remove from friends")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/friends/{id}")
  public ResponseEntity<Map> deleteFromFriends(@PathVariable Long id) {
    try {
      userService.deleteFromFriends(id);
      return ResponseEntity.ok(Map.of("result", true));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of("result", false, "error", e.getMessage()));
    }
  }

}
