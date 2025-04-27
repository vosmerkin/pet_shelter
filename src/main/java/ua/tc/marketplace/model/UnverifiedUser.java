package ua.tc.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * {@code UnverifiedUser} represents a user who has registered but has not yet verified their email address.
 * This entity stores temporary user information required for the verification process.
 *
 * <p>It includes details such as the user's email, a unique verification token sent to their email,
 * and the timestamp of their registration.</p>
 **/

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unverified_users")
public class UnverifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String verificationToken;

    @CreationTimestamp
    private LocalDateTime registrationTimestamp;

}
