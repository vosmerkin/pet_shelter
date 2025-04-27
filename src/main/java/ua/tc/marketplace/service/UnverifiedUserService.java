package ua.tc.marketplace.service;

import ua.tc.marketplace.model.UnverifiedUser;

public interface UnverifiedUserService {


    boolean existByEmail(String email);
    boolean existByToken(String token);

    UnverifiedUser createUser(UnverifiedUser unverifiedUser);

    UnverifiedUser getByToken(String token);
    void delete(Long id);
}
