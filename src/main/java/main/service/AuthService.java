package main.service;

import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import main.dto.request.JwtRequest;
import main.dto.request.RqRegUserDto;
import main.dto.response.JwtResponse;
import main.exception.UserException;
import main.model.User;
import main.repository.UserRepo;
import main.security.JwtAuthentication;
import main.security.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepo userRepo;

  private final PasswordEncoder passwordEncoder;

  private final Map<String, String> refreshStorage = new HashMap<>();

  private final JwtProvider jwtProvider;

  public void register(RqRegUserDto regUserDto) throws UserException {
    User newUser = new User();
    newUser.setEmail(regUserDto.getEmail());
    newUser.setName(regUserDto.getName());
    newUser.setPassword(/*passwordEncoder.encode(*/regUserDto.getPassword());
    try {
      userRepo.save(newUser);
    } catch (DataIntegrityViolationException e) {
      throw  new UserException("Указанный email уже существует");
    }
  }

  public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
    final User user = userRepo.findByEmail(authRequest.getLogin())
        .orElseThrow(() -> new AuthException("Пользователь не найден"));
    if (user.getPassword().equals(authRequest.getPassword())) {
      final String accessToken = jwtProvider.generateAccessToken(user);
      final String refreshToken = jwtProvider.generateRefreshToken(user);
      refreshStorage.put(user.getEmail(), refreshToken);
      return new JwtResponse(accessToken, refreshToken);
    } else {
      throw new AuthException("Неправильный пароль");
    }
  }

  public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      final String login = claims.getSubject();
      final String saveRefreshToken = refreshStorage.get(login);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        final User user = userRepo.findByEmail(login)
            .orElseThrow(() -> new AuthException("Пользователь не найден"));
        final String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse(accessToken, null);
      }
    }
    return new JwtResponse(null, null);
  }

  public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      final String login = claims.getSubject();
      final String saveRefreshToken = refreshStorage.get(login);
      if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
        final User user = userRepo.findByEmail(login)
            .orElseThrow(() -> new AuthException("Пользователь не найден"));
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String newRefreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
      }
    }
    throw new AuthException("Невалидный JWT токен");
  }

  public JwtAuthentication getAuthInfo() {
    return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }

}
