package com.educator.eduo.course.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {
    private String lectureId;
    private String courseId;
    private String lectureName;
    private String lectureDate;
    private String startTime;
    private String endTime;
    private String description;
    private String lectureOrder;
}
