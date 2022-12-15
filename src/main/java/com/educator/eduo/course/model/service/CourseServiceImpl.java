package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import com.educator.eduo.course.model.mapper.CourseMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseMapper courseMapper;
    @Autowired
    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    public List<ThisWeekScheduleDto> selectThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException {
        List<ThisWeekScheduleDto> scheduleList = new ArrayList<>();
        String role = thisWeekRequestDto.getRole();
        if(role.equals("ROLE_TEACHER")) {
            scheduleList = courseMapper.selectTeacherWeekScheduleByUserIdWithDate(thisWeekRequestDto);
        } else if(role.equals("ROLE_ASSISTANT")) {
            scheduleList = courseMapper.selectAssistantWeekScheduleByUserIdWithDate(thisWeekRequestDto);
        } else if(role.equals("ROLE_STUDENT")) {
            scheduleList = courseMapper.selectStudentWeekScheduleByUserIdWithDate(thisWeekRequestDto);
        }
        if(scheduleList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
        return scheduleList;
    }

    @Override
    @Transactional
    public List<CourseInfoDto> selectCourseByCourseId(String userId, String role) throws NotFoundException {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        if(role.equals("ROLE_TEACHER")) {
            courseInfoList = courseMapper.selectTeacherCourseByUserId(userId);
        } else if(role.equals("ROLE_ASSISTANT")) {
            courseInfoList = courseMapper.selectAssistantCourseByUserId(userId);
        } else if(role.equals("ROLE_STUDENT")) {
            courseInfoList = courseMapper.selectStudentCourseByUserId(userId);
        }
        if(courseInfoList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
        return courseInfoList;
    }
}
