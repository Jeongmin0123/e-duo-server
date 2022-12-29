package com.educator.eduo.attendance.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educator.eduo.attendance.model.dto.AttendanceRequestDto;
import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;
import com.educator.eduo.attendance.model.mapper.AttendanceMapper;
import com.educator.eduo.lecture.model.mapper.LectureMapper;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);
	
	private final AttendanceMapper attendanceMapper;
	private final LectureMapper lectureMapper;
	
	@Autowired
	public AttendanceServiceImpl(AttendanceMapper attendanceMapper, LectureMapper lectureMapper) {
		this.attendanceMapper = attendanceMapper;
		this.lectureMapper = lectureMapper;
	}
	@Override
	public List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException {
		List<AttendanceResultDto> attendanceResultList = attendanceMapper.selectAttendanceList(lectureId);
		if(attendanceResultList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
		return attendanceResultList;
	}
	
	@Override
	public boolean updateAttendance(List<AttendanceRequestDto> attendancelist) throws SQLException {
		return attendanceMapper.updateAttendance(attendancelist);
	}
	
	@Override
	public boolean updateAssignment(List<AttendanceRequestDto> attendancelist) throws SQLException {
		return attendanceMapper.updateAssignment(attendancelist);
	}
	@Override
	public boolean updateTestScore(AttendanceRequestDto attendance) throws SQLException {
		return attendanceMapper.updateTestScore(attendance);
	}
	@Override
	@Transactional
	public boolean registerAssignment(Attendance attendance) throws SQLException {
		String lectureId = attendance.getLectureId();
		lectureMapper.modifyExistAssignment(lectureId);
		return attendanceMapper.registerAssignment(attendance);
	}
}
