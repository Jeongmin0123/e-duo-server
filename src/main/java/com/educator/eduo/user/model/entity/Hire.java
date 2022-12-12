package com.educator.eduo.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hire {
    private String assistantUserId;
    private String teacherUserId;
    private String state;
}
