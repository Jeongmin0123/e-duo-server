package com.educator.eduo.attendance.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;

public interface AttendanceService {

	List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException;

	boolean updateAttendance(Attendance attendance) throws SQLException;

	boolean updateAssignment(Attendance attendance) throws SQLException;

	boolean updatetestScore(Attendance attendance) throws SQLException;

}
