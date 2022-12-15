package com.educator.eduo.course.model.dto;

import com.educator.eduo.course.model.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRegisterDto {
    private Course course;
    private List<ScheduleRegisterDto> scheduleList;
}
