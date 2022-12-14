package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseResultDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface CourseService {
    List<CourseResultDto> selectTeacherCourse(String userId) throws NotFoundException;

    List<ThisWeekScheduleDto> selectTeacherThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException;

    List<CourseResultDto> selectAssistantCourse(String userId) throws NotFoundException;

    List<CourseResultDto> selectStudentCourse(String userId) throws NotFoundException;
}
