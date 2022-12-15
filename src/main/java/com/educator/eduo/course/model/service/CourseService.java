package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface CourseService {
    List<ThisWeekScheduleDto> selectTeacherThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException;
    List<ThisWeekScheduleDto> selectAssistantThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException;
    List<ThisWeekScheduleDto> selectStudentThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException;

    List<CourseInfoDto> selectTeacherCourse(String userId) throws NotFoundException;

    List<CourseInfoDto> selectAssistantCourse(String userId) throws NotFoundException;

    List<CourseInfoDto> selectStudentCourse(String userId) throws NotFoundException;

}
