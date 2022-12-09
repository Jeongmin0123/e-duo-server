package com.educator.eduo.sms.model.service;

import com.educator.eduo.sms.model.dto.SmsRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsServiceImpl implements SmsService {

    private final String requestUrl;
    private final String signingUri;
    private final String accessKey;
    private final String secretKey;

    public SmsServiceImpl(
            @Value("${naver.sens.url}") String requestUrl,
            @Value("${naver.sens.uri}") String signingUri,
            @Value("${naver.sens.access-key}") String accessKey,
            @Value("${naver.sens.secret-key}") String secretKey
    ) {
        this.requestUrl = requestUrl;
        this.signingUri = signingUri;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public void requestSmsToNaver(SmsRequestDto smsRequestDto, String timestamp)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        HttpHeaders headers = generateHttpHeaders(timestamp);
        RestTemplate restTemplate = generateRestTemplate();

        HttpEntity<String> request = new HttpEntity<>(smsRequestDto.toJson(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);

        if (response.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new RuntimeException("문자 전송을 실패했습니다.");
        }
    }

    private HttpHeaders generateHttpHeaders(String timestamp) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", timestamp);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(HttpMethod.POST.name(), signingUri, timestamp, accessKey, secretKey));
        return headers;
    }

    private String makeSignature(String method, String signUri, String timestamp, String accessKey, String secretKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String message = new StringBuilder().append(method).append(" ")
                                            .append(signUri).append("\n")
                                            .append(timestamp).append("\n")
                                            .append(accessKey)
                                            .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeBase64String(rawHmac);
    }

    private RestTemplate generateRestTemplate() {
        HttpClient httpClient = HttpClientBuilder.create()
                                                 .setMaxConnTotal(100)
                                                 .setMaxConnPerRoute(5)
                                                 .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(3000);

        return new RestTemplate(factory);
    }

}
