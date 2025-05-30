package ua.tc.marketplace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tc.marketplace.model.entity.User;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**

 **/

//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    public enum TokenType {
        REGISTRATION,
        PASSWORD_RESET,
        EMAIL_CHANGE,
        TWO_FACTOR
    }
    private static final int EXPIRATION = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="verification_token")
    @NotNull
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name="token_type")
    @NotNull
    private TokenType type;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @NotNull
    private User user;

    @NotNull
    private Date expiryDate;

    public VerificationToken(User user, int tokenExpiryTimeInMinutes) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate(tokenExpiryTimeInMinutes);
        this.type=TokenType.REGISTRATION;
    }

    public VerificationToken( User user, TokenType type) {
        this.type = type;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int tokenExpiryTimeInMinutes) {
        return Date.from(Instant.now().plusSeconds(tokenExpiryTimeInMinutes* 60L));
//        da
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Timestamp(cal.getTime()));
//        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//        return new Date(cal.getTime().getTime());
    }



    // standard constructors, getters and setters
}