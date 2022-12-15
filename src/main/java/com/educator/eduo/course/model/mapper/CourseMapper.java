package com.educator.eduo.course.model.mapper;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseInfoDto> selectTeacherCourseByUserId(String userId);
    List<CourseInfoDto> selectAssistantCourseByUserId(String userId);
    List<CourseInfoDto> selectStudentCourseByUserId(String userId);


    List<ThisWeekScheduleDto> selectTeacherWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto);

    List<ThisWeekScheduleDto> selectAssistantWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto);

    List<ThisWeekScheduleDto> selectStudentWeekScheduleByUserIdWithDate(ThisWeekRequestDto thisWeekRequestDto);
}
