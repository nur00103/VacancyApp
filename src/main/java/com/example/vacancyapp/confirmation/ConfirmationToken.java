package com.example.vacancyapp.confirmation;

import com.example.vacancyapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationToken {
    public static final long TOKEN_EXPIRED_TIME = 900;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expiredAt;
    private String email;

    public ConfirmationToken(User user) {
        this.email = user.getMail();
        this.token = UUID.randomUUID().toString();
        this.expiredAt = Date.from(Instant.now().plusSeconds(TOKEN_EXPIRED_TIME));
    }
}
