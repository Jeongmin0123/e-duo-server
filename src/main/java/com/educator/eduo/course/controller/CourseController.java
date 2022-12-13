package com.educator.eduo.course.controller;

import com.educator.eduo.course.model.dto.CourseResultDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import com.educator.eduo.course.model.service.CourseService;
import lombok.SneakyThrows;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CourseController {
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/api/teacher-weekschedule")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> teacherWeekSchedule(@RequestBody ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException {
        logger.info("CourseController : {} ",thisWeekRequestDto);
        List<ThisWeekScheduleDto> scheduleDtoList = courseService.selectTeacherThisWeekSchedule(thisWeekRequestDto);
        return ResponseEntity.ok(scheduleDtoList);
    }

    @GetMapping("/api/teacher-courses")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> teacherSchedule(@RequestBody String userId) throws NotFoundException {
        List<CourseResultDto> courseResultList = courseService.selectTeacherSchedule(userId);
        return ResponseEntity.ok(courseResultList);
    }
}
