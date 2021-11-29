package com.nezzar064.ocafe.security.service;

import com.nezzar064.ocafe.exception.DataNotFoundException;
import com.nezzar064.ocafe.exception.TokenRefreshException;
import com.nezzar064.ocafe.model.entity.RefreshToken;
import com.nezzar064.ocafe.model.entity.User;
import com.nezzar064.ocafe.repository.RefreshTokenRepository;
import com.nezzar064.ocafe.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@PropertySource(value = {"classpath:application.properties"})
public class RefreshTokenService {

  private RefreshTokenRepository refreshTokenRepository;
  private UserRepository userRepository;

  @Autowired
  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User with id: " + userId + " not found"));

    if (refreshTokenAlreadyPresent(user)) {
      return refreshTokenRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Refresh token for User: " + user + " can't be found!"));
    }
    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  private boolean refreshTokenAlreadyPresent(User user) {
    return refreshTokenRepository.existsByUser(user);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new SignIn request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User with id: " + userId + " not found"));
    return refreshTokenRepository.deleteByUser(user);
  }
}