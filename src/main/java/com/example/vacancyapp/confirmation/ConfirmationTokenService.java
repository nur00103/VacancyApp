package com.example.vacancyapp.confirmation;

import com.example.vacancyapp.entity.User;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final JavaMailSender javaMailSender;

    @Value("${address}")
    private String appHost;

    @Transactional
    @Async
    public void sendConfirmationMail(User user) throws MessagingException, UnsupportedEncodingException {
        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        ConfirmationToken savedConfirmToken=confirmationTokenRepo.save(confirmationToken);
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(message);
        mimeMessageHelper.setFrom(new InternetAddress("verifificationprov@gmail.com","Vacancy"));
        mimeMessageHelper.setTo(confirmationToken.getEmail());
        mimeMessageHelper.setSubject("Confirmation mail");
        mimeMessageHelper.setText(getConfirmMessage(user, savedConfirmToken), true);
        javaMailSender.send(message);
    }
    private String getConfirmMessage(User user, ConfirmationToken confirmationToken) {
        String link = appHost + "/user/confirmation/" + confirmationToken.getToken();
        String message = "<body>\n" +
                "<h3>Welcome, " + user.getName().concat(" " + user.getSurname()) + "</h3>\n" +
                "<div>Please click <a href='" + link + "'>here</a> and confirm your email address</div>\n" +
                "</body>\n" +
                "</html>";
        return message;
    }

    public ConfirmationToken getByToken(String token) throws MyException {
        ConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token)
                .orElseThrow(() ->  new MyException(ExceptionEnum.NOT_VALID_TOKEN));
        validateToken(confirmationToken);
        return confirmationToken;
    }
    private void validateToken(ConfirmationToken confirmToken) throws MyException {
        if (confirmToken.getExpiredAt().before(Date.from(Instant.now()))) {
            throw new MyException(ExceptionEnum.EXPIRED_TOKEN);
        }
    }
    public ConfirmationToken getByEmail(String email){
        return confirmationTokenRepo.findByEmail(email)
                .orElseThrow(()->new MyException(ExceptionEnum.USER_NOT_FOUND));
    }

}
