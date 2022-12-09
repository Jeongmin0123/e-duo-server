package com.educator.eduo.sms.model.service;

import com.educator.eduo.sms.model.dto.SmsRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {

    void requestSmsToNaver(SmsRequestDto smsRequestDto, String timestamp)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

}
