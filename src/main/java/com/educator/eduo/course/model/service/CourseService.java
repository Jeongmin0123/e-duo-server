package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface CourseService {

    List<CourseInfoDto> selectCourseByCourseId(String userId, String role) throws NotFoundException;

    List<ThisWeekScheduleDto> selectThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException;
}
