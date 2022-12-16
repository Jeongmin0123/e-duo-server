package com.educator.eduo.course.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;
    private String courseName;
    private String academyName;
    private String academyAddress;
    private String startDate;
    private String endDate;
    private String description;
}
