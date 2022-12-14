package com.educator.eduo.course.model.dto;

import com.educator.eduo.course.model.entity.Course;
import com.educator.eduo.course.model.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResultDto {
    @JsonIgnore
    private String courseId;
    private Course course;
    private List<Schedule> scheduleList;

}
