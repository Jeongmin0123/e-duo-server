package com.educator.eduo.course.model.mapper;

import com.educator.eduo.course.model.dto.CourseResultDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseResultDto> selectTeacherCourseByCourseId(String userId);
    List<CourseResultDto> selectAssistantCourseByCourseId(String userId);
    List<CourseResultDto> selectStudentCourseByCourseId(String userId);


}
