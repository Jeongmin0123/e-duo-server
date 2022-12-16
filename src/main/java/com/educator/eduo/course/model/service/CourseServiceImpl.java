package com.educator.eduo.course.model.service;

import com.educator.eduo.course.model.dto.*;
import com.educator.eduo.course.model.entity.Course;
import com.educator.eduo.course.model.mapper.CourseMapper;
import com.educator.eduo.lecture.model.entity.Lecture;
import com.educator.eduo.lecture.model.mapper.LectureMapper;
import com.educator.eduo.util.CodeGenerator;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CourseServiceImpl implements CourseService{
    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseMapper courseMapper;
    private final LectureMapper lectureMapper;
    @Autowired
    public CourseServiceImpl(CourseMapper courseMapper, LectureMapper lectureMapper) {
        this.courseMapper = courseMapper;
        this.lectureMapper = lectureMapper;
    }

    @Override
    @Transactional
    public List<ThisWeekScheduleDto> selectThisWeekSchedule(ThisWeekRequestDto thisWeekRequestDto) throws NotFoundException, SQLException {
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
    public List<CourseInfoDto> selectCourseByCourseId(String userId, String role) throws NotFoundException, SQLException {
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

    @Override
    @Transactional
    public void registerCourse(CourseRegisterDto courseRegisterDto) throws SQLException {
        // course, shcedule 먼저 insert
        String userId = courseRegisterDto.getCourse().getUserId();
        Course course = extractCourse(courseRegisterDto, userId);
        courseMapper.insertCourse(course);

        // Schedule 배열 만들기 & Schedule 등록
        List<ScheduleRegisterDto> scheduleRegisterDtoList = courseRegisterDto.getScheduleList();
        ScheduleRegisterDto[] scheduleArr = makeScheduleArr(course, scheduleRegisterDtoList);
        courseMapper.insertSchedules(scheduleRegisterDtoList);

        // Lecture 만들기 & lecture 등록
        List<Lecture> lectureList = makeLectureList(courseRegisterDto, course, scheduleArr);
        lectureMapper.insertAllLecture(lectureList);
        return;
    }

    private ScheduleRegisterDto[] makeScheduleArr(Course course, List<ScheduleRegisterDto> scheduleRegisterDtoList) {
        ScheduleRegisterDto[] scheduleArr = new ScheduleRegisterDto[8];
        for (ScheduleRegisterDto scheduleInfo : scheduleRegisterDtoList) {
            scheduleInfo.setCourseId(course.getCourseId());
            int day = scheduleInfo.getDay();
            scheduleArr[day] = scheduleInfo;
        }
        return scheduleArr;
    }

    private List<Lecture> makeLectureList(CourseRegisterDto courseRegisterDto, Course course, ScheduleRegisterDto[] scheduleArr) {
        List<Lecture> lectureList = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(courseRegisterDto.getCourse().getStartDate());
        LocalDate endDate = LocalDate.parse(courseRegisterDto.getCourse().getEndDate());
        LocalDate date = startDate.minusDays(1);
        int week = 1, order = 1;
        StringBuilder sb;
        while ((date = date.plusDays(1)).isBefore(endDate.plusDays(1))) {
            ScheduleRegisterDto info = scheduleArr[date.getDayOfWeek().getValue()];
            if(info != null) {
                sb = new StringBuilder();
                sb.append(week).append("주차 ").append(order++).append("차 : ").append(course.getCourseName());
                lectureList.add(
                        buildLecture(course, date, sb, info)
                );
            }
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                week+=1;
                order = 1;
            }
        }
        return lectureList;
    }

    private Lecture buildLecture(Course course, LocalDate date, StringBuilder sb, ScheduleRegisterDto info) {
        return Lecture.builder()
                .courseId(course.getCourseId())
                .lectureName(sb.toString())
                .lectureDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .startTime(info.getStartTime())
                .endTime(info.getEndTime())
                .existAssignment(info.isExistAssignment())
                .existTest(info.isExistTest())
                .testType(info.getTestType())
                .build();
    }

    private Course extractCourse(CourseRegisterDto courseRegisterDto, String userId) {
        String courseId = createCourseId(userId);
        Course course = courseRegisterDto.getCourse();
        course.setCourseId(courseId);
        return course;
    }

    private String createCourseId(String userId) {
        StringBuilder courseId = new StringBuilder();
        courseId.append(CodeGenerator.generateCode(3)).append("-").append(userId.substring(0,5)).append("-").append(CodeGenerator.generateCode(4));
        return courseId.toString();
    }
}
