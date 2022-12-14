package com.educator.eduo.lecture.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.educator.eduo.course.controller.CourseController;
import com.educator.eduo.lecture.model.entity.Lecture;
import com.educator.eduo.lecture.model.service.LectureService;

@RestController
@CrossOrigin("*")
public class LectureController {

	Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    
    @PostMapping("/api/lecture")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> insertLecture(@RequestBody Lecture lecture) throws SQLException {
    	if(lectureService.insertLecture(lecture)) {
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @PutMapping("/api/lecture")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> modifyLecture(@RequestBody Lecture lecture) throws SQLException {
    	if(lectureService.updateLecture(lecture)) {
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @DeleteMapping("/api/lecture")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> deleteLecture(@RequestBody String lectureId) throws SQLException {
    	if(lectureService.deleteLecture(lectureId)) {
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
}
