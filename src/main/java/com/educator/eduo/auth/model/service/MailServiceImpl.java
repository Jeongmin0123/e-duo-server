package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.AuthMailDto;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Override
    public void sendEmailAuthCode(AuthMailDto mailDto) throws MessagingException, MailException {
        logger.info("sendEmailAuthCode : {} ", mailDto);
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
