package com.educator.eduo.course.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    private String courseId;
    @JsonIgnore
    private String userId;
    private String courseName;
    private String academyName;
    private String academyAddress;
    private String startDate;
    private String endDate;
}
