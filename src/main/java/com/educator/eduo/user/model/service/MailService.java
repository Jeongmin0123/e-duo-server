package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.AuthMailDto;
import javax.mail.MessagingException;
import org.springframework.mail.MailException;

public interface MailService {

    void sendEmailAuthCode(AuthMailDto mailDto) throws MessagingException, MailException;

}
