package com.educator.eduo.course.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRegisterDto {
    private String courseId;
    private int day;
    private String startTime;
    private String endTime;
    private boolean existAssignment;
    private boolean existTest;
    private String testType;
}
