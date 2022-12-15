package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.CourseRegisterDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import org.apache.ibatis.javassist.NotFoundException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface CourseService {

    List<CourseInfoDto> selectCourseByCourseId(String userId, String role) throws NotFoundException, SQLException;

    List<ThisWeekScheduleDto> selectThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException, SQLException;

    void registerCourse(CourseRegisterDto courseRegisterDto) throws SQLException;
}
