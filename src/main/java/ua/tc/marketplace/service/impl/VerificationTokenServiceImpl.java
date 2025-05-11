package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.exception.verificationToken.VerificationTokenNotFoundException;
import ua.tc.marketplace.model.VerificationToken;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.repository.VerificationTokenRepository;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.service.VerificationTokenService;

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

    @Override
    public void clearExpiredTokens() {
        List<VerificationToken> tokenList= verificationTokenRepository.findAll();
        if (tokenList != null && !tokenList.isEmpty()) {
            for(VerificationToken token:tokenList){
                User user = token.getUser();
                if (userService.UserExistsByEmail(user.getEmail()))
                    userService.deleteUserById(user.getId());
                delete(token.getId());
            }
        }
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        log.info("Requested VerificationToken by token {}", token);
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting verificationToken with id={}", id);
        VerificationToken existingToken = getById(id);
            verificationTokenRepository.deleteById(existingToken.getId());
    }
}
