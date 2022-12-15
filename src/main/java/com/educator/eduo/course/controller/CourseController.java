package com.educator.eduo.course.controller;

import com.educator.eduo.course.model.dto.CourseInfoDto;
import com.educator.eduo.course.model.dto.CourseRegisterDto;
import com.educator.eduo.course.model.dto.ThisWeekRequestDto;
import com.educator.eduo.course.model.dto.ThisWeekScheduleDto;
import com.educator.eduo.course.model.service.CourseService;
import com.educator.eduo.util.CodeGenerator;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
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

    @PostMapping("/api/weekschedule")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> getWeekSchedule(@RequestBody ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException, SQLException {
        logger.info("ThisWeekSchedule Rqeust : {}", thisWeekRequestDto);
        List<ThisWeekScheduleDto> scheduleList = courseService.selectThisWeekSchedule(thisWeekRequestDto);
        return ResponseEntity.ok(scheduleList);
    }

    @GetMapping("/api/courses")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> getCourses(@RequestParam("userId") String userId, @RequestParam("role") String role) throws NotFoundException, SQLException {
        logger.info("getCourses parameter : {}, {}", userId, role);
        List<CourseInfoDto> courseInfoList = courseService.selectCourseByCourseId(userId, role);
        return ResponseEntity.ok(courseInfoList);
    }

    @PostMapping("/api/course")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> registerCourse(@RequestBody CourseRegisterDto courseRegisterDto) throws SQLException {
        logger.info("registerCourse : {} ",courseRegisterDto);
        courseService.registerCourse(courseRegisterDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
