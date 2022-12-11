package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.AuthMailDto;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailAuthCode(AuthMailDto mailDto) throws MessagingException, MailException {
        MimeMessage message = createMail(mailDto);
        mailSender.send(message);
    }

    private MimeMessage createMail(AuthMailDto mailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(mailDto.getFrom()));
        message.addRecipients(RecipientType.TO, mailDto.getTo());
        message.setSubject(mailDto.getSubject());
        message.setText(mailDto.getContent(), "UTF-8", "HTML");
        return message;
    }

}
