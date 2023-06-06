package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import main.dto.request.JwtRequest;
import main.dto.request.RefreshJwtRequest;
import main.dto.request.RqRegUserDto;
import main.dto.response.JwtResponse;
import main.dto.response.RsBasic;
import main.dto.response.RsToken;
import main.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "User registration")
  @PostMapping("reg")
  public ResponseEntity<RsBasic> register(@RequestBody RqRegUserDto regUserDto) {
    try {
      authService.register(regUserDto);
      return ResponseEntity.ok(RsBasic.getSuccessInstance());
    } catch (Exception e) {
      return ResponseEntity.status(400).body(RsBasic.getUnSuccessInstance(e.getMessage()));
    }
  }

  @Operation(summary = "User login")
  @PostMapping("login")
  public ResponseEntity<RsToken> login(@RequestBody JwtRequest authRequest) {
    try {
      JwtResponse token = authService.login(authRequest);
      return ResponseEntity.ok(RsToken.getSuccessInstance(token.getAccessToken()));
    } catch (AuthException e) {
      return ResponseEntity.status(400).body(RsToken.getUnSuccessInstance(e.getMessage()));
    }
  }

  @PostMapping("token")
  public ResponseEntity<RsToken> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
    try {
      JwtResponse token = authService.getAccessToken(request.getRefreshToken());
      return ResponseEntity.ok(RsToken.getSuccessInstance(token.getAccessToken()));
    } catch (AuthException e) {
      return ResponseEntity.status(400).body(RsToken.getUnSuccessInstance(e.getMessage()));
    }
  }

  @PostMapping("refresh")
  public ResponseEntity<RsToken> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
    try {
      JwtResponse token = authService.refresh(request.getRefreshToken());
      return ResponseEntity.ok(RsToken.getSuccessInstance(token.getAccessToken()));
    } catch (AuthException e) {
      return ResponseEntity.status(400).body(RsToken.getUnSuccessInstance(e.getMessage()));
    }
  }

}
