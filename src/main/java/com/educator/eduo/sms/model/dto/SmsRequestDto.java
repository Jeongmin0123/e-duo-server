package com.educator.eduo.sms.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDto {

    private static final String DEFAULT_FROM = "하나 등록해두어야 함";
    private static final String DEFAULT_CONTENT = "[Eduo] SMS 전송을 실패하였습니다.";

    private String type;
    private String from;
    private String content;
    private List<Message> messages;

    public SmsRequestDto(String type, Message message) {
        this.type = type;
        this.from = DEFAULT_FROM;
        this.content = DEFAULT_CONTENT;
        this.messages = new ArrayList<>();
        messages.add(message);
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}
