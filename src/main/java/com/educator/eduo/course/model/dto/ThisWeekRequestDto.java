package com.educator.eduo.course.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThisWeekRequestDto {
    private String userId;
    private String role;
    private String monday;
    private String sunday;
}
