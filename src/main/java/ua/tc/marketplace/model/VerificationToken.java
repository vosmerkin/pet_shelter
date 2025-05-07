package ua.tc.marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tc.marketplace.model.entity.User;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**

 **/

//@Builder
@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public VerificationToken(User user, int tokenExpiryTimeInMinutes) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate(tokenExpiryTimeInMinutes);
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