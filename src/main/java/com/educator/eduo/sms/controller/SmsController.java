package com.educator.eduo.sms.controller;

import com.educator.eduo.sms.model.dto.Message;
import com.educator.eduo.sms.model.dto.SmsRequestDto;
import com.educator.eduo.sms.model.service.SmsService;
import com.educator.eduo.util.NumberGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/auth/phone")
    public ResponseEntity<?> sendPhoneAuthCode(@RequestBody Map<String, String> numberMap)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String to = numberMap.get("phone");
        String smsAuthCode = NumberGenerator.generateRandomUniqueNumber(6);
        Message sms = new Message(to, String.format("[Eduo] 인증번호는 %s입니다.", smsAuthCode));

        SmsRequestDto smsRequestDto = new SmsRequestDto("SMS", sms);
        smsService.sendPhoneAuthCode(smsRequestDto, String.valueOf(System.currentTimeMillis()));

        return new ResponseEntity<>(smsAuthCode, HttpStatus.OK);
    }

}
