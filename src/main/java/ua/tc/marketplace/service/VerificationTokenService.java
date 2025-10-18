package ua.tc.marketplace.service;

import ua.tc.marketplace.model.VerificationToken;

import java.util.List;

public interface VerificationTokenService {

  List<VerificationToken> getAll() ;
  VerificationToken getById(Long id);
  void clearExpiredTokens();
  VerificationToken getVerificationToken(String token);
  void delete(Long id);
  boolean verifyToken(VerificationToken token, VerificationToken.TokenType tokenType);
}

