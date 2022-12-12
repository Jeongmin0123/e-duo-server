package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseResultDto;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface CourseService {
    List<CourseResultDto> selectTeacherSchedule(String userId) throws NotFoundException;
}
