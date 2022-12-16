package com.educator.eduo.attendence.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.educator.eduo.attendence.model.dto.AttendanceResultDto;
import com.educator.eduo.attendence.model.entity.Attendance;
import com.educator.eduo.attendence.model.service.AttendanceService;

@RestController
@CrossOrigin("*")
public class AttendanceController {

	Logger logger = LoggerFactory.getLogger(AttendanceController.class);
	
	private final AttendanceService attendanceService;
	
	@Autowired
	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}
	
	@GetMapping("/api/attendance/{lectureId}")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> selectAttendanceList(@PathVariable("lectureId") String lectureId) throws NotFoundException {
		List<AttendanceResultDto> attendanceResultList = attendanceService.selectAttendanceList(lectureId);
		return new ResponseEntity<>(attendanceResultList,HttpStatus.OK);
	}
	
	@PutMapping("/api/attendance")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> updateAttendance(@RequestBody Attendance attendance) throws SQLException {
		if(!attendanceService.updateAttendance(attendance)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}
