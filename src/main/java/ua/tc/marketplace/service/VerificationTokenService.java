package ua.tc.marketplace.service;

import ua.tc.marketplace.model.VerificationToken;

public interface VerificationTokenService {

  void clearExpiredTokens();
  VerificationToken getVerificationToken(String token);
  void delete(Long id);
}

