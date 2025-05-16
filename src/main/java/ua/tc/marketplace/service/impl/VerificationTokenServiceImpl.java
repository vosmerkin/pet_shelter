package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tc.marketplace.exception.verificationToken.VerificationTokenNotFoundException;
import ua.tc.marketplace.model.VerificationToken;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.repository.VerificationTokenRepository;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.service.VerificationTokenService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    @Override
    public VerificationToken getById(Long id) {
        return verificationTokenRepository.findById(id)
                .orElseThrow(() -> new VerificationTokenNotFoundException(id));
    }

    @Transactional
    @Override
    public void clearExpiredTokens() {
        List<VerificationToken> tokenList = verificationTokenRepository.findAll();
        if (!tokenList.isEmpty()) {
            for (VerificationToken token : tokenList) {
                if (token.getExpiryDate().before(Date.from(Instant.now()))) { //token is expired
                    log.debug("Expiry date - {}", token.getExpiryDate());
                    log.debug("Current date - {}", Date.from(Instant.now()));
                    User user = token.getUser();
                    if (userService.UserExistsByEmail(user.getEmail()))
                        userService.deleteUserById(user.getId());
                    delete(token.getId());
                }
            }
        }
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        log.info("Requested VerificationToken by token {}", token);
        return verificationTokenRepository.findByToken(token).orElseThrow(()->new VerificationTokenNotFoundException(token));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting verificationToken with id={}", id);
        VerificationToken existingToken = getById(id);
        verificationTokenRepository.deleteById(existingToken.getId());
    }
}
