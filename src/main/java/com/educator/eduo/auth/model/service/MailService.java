package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.AuthMailDto;
import javax.mail.MessagingException;
import org.springframework.mail.MailException;

public interface MailService {

    void sendEmailAuthCode(AuthMailDto mailDto) throws MessagingException, MailException;

}
