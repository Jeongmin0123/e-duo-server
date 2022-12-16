package com.educator.eduo.attendence.controller;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.educator.eduo.attendence.model.dto.AttendanceResultDto;
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
	public ResponseEntity<?> selectAttendanceList(@PathVariable("lectureId") String lectureId) throws NotFoundException {
		List<AttendanceResultDto> attendanceResultList = attendanceService.selectAttendanceList(lectureId);
		return new ResponseEntity<>(attendanceResultList,HttpStatus.OK);
	}
}
