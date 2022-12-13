package com.educator.eduo.course.model.dto;

import com.educator.eduo.course.model.entity.Course;
import com.educator.eduo.course.model.entity.Lecture;
import com.educator.eduo.course.model.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThisWeekScheduleDto {
    private Course course;
    private List<Lecture> lectureList;
}
