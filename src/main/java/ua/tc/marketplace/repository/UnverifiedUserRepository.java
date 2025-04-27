package ua.tc.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tc.marketplace.model.UnverifiedUser;

import java.util.Optional;

public interface UnverifiedUserRepository extends JpaRepository<UnverifiedUser, Long> {
    Optional<UnverifiedUser> findByVerificationToken(String token);
    Optional<UnverifiedUser> findByEmail(String email);
}
