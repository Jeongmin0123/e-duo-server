package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.CourseResultDto;
import com.educator.eduo.course.model.mapper.CourseMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<CourseResultDto> selectTeacherSchedule(String userId) throws NotFoundException{
        List<CourseResultDto> courseResultList = courseMapper.selectTeacherCourseByCourseId(userId);
        logger.info("Teacher Schedule is : {}", courseResultList);
        if(courseResultList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
        return courseResultList;
    }
}
