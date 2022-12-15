package com.educator.eduo.course.model.mapper;

import com.educator.eduo.course.model.dto.*;
import com.educator.eduo.course.model.entity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseInfoDto> selectTeacherCourseByUserId(String userId) throws SQLException;
    List<CourseInfoDto> selectAssistantCourseByUserId(String userId) throws SQLException;
    List<CourseInfoDto> selectStudentCourseByUserId(String userId) throws SQLException;


    List<ThisWeekScheduleDto> selectTeacherWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto) throws SQLException;

    List<ThisWeekScheduleDto> selectAssistantWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto) throws SQLException;

    List<ThisWeekScheduleDto> selectStudentWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto) throws SQLException;


    void insertCourse(Course course) throws SQLException;

    void insertSchedules(List<ScheduleRegisterDto> scheduleRegisterDtoList) throws SQLException;
}
