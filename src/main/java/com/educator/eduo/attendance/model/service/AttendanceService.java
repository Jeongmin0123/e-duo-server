package com.educator.eduo.attendance.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendance.model.dto.AttendanceRequestDto;
import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;

public interface AttendanceService {

	List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException;

	boolean updateAttendance(AttendanceRequestDto attendance) throws SQLException;

	boolean updateAssignment(AttendanceRequestDto attendance) throws SQLException;

	boolean updateTestScore(AttendanceRequestDto attendance) throws SQLException;

	boolean registerAssignment(Attendance attendance) throws SQLException;

}
