package com.educator.eduo.course.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    private int scheduleId;
    private String courseId;
    private int day;
    private String startTime;
    private String endTime;
}
