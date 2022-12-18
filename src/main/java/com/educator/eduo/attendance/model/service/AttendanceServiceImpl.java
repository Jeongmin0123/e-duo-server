package com.educator.eduo.attendance.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;
import com.educator.eduo.attendance.model.mapper.AttendanceMapper;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);
	
	private final AttendanceMapper attendanceMapper;
	
	@Autowired
	public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
		this.attendanceMapper = attendanceMapper;
	}
	@Override
	public List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException {
		List<AttendanceResultDto> attendanceResultList = attendanceMapper.selectAttendanceList(lectureId);
		if(attendanceResultList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
		return attendanceResultList;
	}
	
	@Override
	public boolean updateAttendance(Attendance attendance) throws SQLException {
		return attendanceMapper.updateAttendance(attendance);
	}
	
	@Override
	public boolean updateAssignment(Attendance attendance) throws SQLException {
		return attendanceMapper.updateAssignment(attendance);
	}
	@Override
	public boolean updatetestScore(Attendance attendance) throws SQLException {
		return attendanceMapper.updatetestScore(attendance);
	}
}
