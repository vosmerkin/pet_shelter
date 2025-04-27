package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.model.UnverifiedUser;
import ua.tc.marketplace.repository.UnverifiedUserRepository;
import ua.tc.marketplace.service.UnverifiedUserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnverifiedUserServiceImpl implements UnverifiedUserService {
    private final UnverifiedUserRepository unverifiedUserRepository;

    @Override
    public boolean existByEmail(String email) {
        return false;
    }

    @Override
    public boolean existByToken(String token) {
        return false;
    }

    @Override
    public UnverifiedUser createUser(UnverifiedUser unverifiedUser) {
        return null;
    }

    @Override
    public UnverifiedUser getByToken(String token) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
