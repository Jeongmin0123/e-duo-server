package com.educator.eduo.attendance.controller;

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

import com.educator.eduo.attendance.model.dto.AttendanceRequestDto;
import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;
import com.educator.eduo.attendance.model.service.AttendanceService;

@RestController
@CrossOrigin("*")
public class AttendanceController {

	Logger logger = LoggerFactory.getLogger(AttendanceController.class);
	
	private final AttendanceService attendanceService;
	
	@Autowired
	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}
	
	// 과제 입력
	@PutMapping("/api/assignment/register")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> registerAssignment(@RequestBody Attendance attendance) throws SQLException {
		if(!attendanceService.registerAssignment(attendance)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	// 출결, 과제, 점수 조회
	@GetMapping("/api/attendance/{lectureId}")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> selectAttendanceList(@PathVariable("lectureId") String lectureId) throws NotFoundException {
		List<AttendanceResultDto> attendanceResultList = attendanceService.selectAttendanceList(lectureId);
		return new ResponseEntity<>(attendanceResultList,HttpStatus.OK);
	}
	
	// 출석 상태 변경
	@PutMapping("/api/attendance")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> updateAttendance(@RequestBody AttendanceRequestDto attendance) throws SQLException {
		if(!attendanceService.updateAttendance(attendance)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	// 과제 제출 상태 변경
	@PutMapping("/api/assignment")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> updateAssignment(@RequestBody AttendanceRequestDto attendance) throws SQLException {
		if(!attendanceService.updateAssignment(attendance)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	// 점수 상태 변경
	@PutMapping("/api/testScore")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ASSISTANT')")
	public ResponseEntity<?> updateTestScore(@RequestBody AttendanceRequestDto attendance) throws SQLException {
		if(!attendanceService.updateTestScore(attendance)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}
