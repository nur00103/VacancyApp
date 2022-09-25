package com.example.vacancyapp.confirmation;

import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender{

    private final static Logger LOGGER= LoggerFactory.getLogger(EmailSenderImpl.class);
    private JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String to, String email) {
       try{
           MimeMessage mimeMessage=javaMailSender.createMimeMessage();
           MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
           helper.setText(email,true);
           helper.setTo(to);
           helper.setSubject("Confirm your email");
           helper.setFrom("verifificationprov@gmail.com");
       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }
    }
}
