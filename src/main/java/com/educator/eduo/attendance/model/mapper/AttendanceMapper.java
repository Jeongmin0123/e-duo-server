package com.educator.eduo.attendance.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendance.model.dto.AttendanceRegisterDto;
import com.educator.eduo.attendance.model.dto.AttendanceRequestDto;
import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.entity.Attendance;
import com.educator.eduo.sugang.model.dto.SugangRequestDto;

@Mapper
public interface AttendanceMapper {

	List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException;

	boolean updateAttendance(AttendanceRequestDto attendance) throws SQLException;

	boolean updateAssignment(AttendanceRequestDto attendance) throws SQLException;

	boolean updateTestScore(AttendanceRequestDto attendance) throws SQLException;
	
	void insertAttendanceList(List<AttendanceRegisterDto> attendanceList) throws SQLException;

	void deleteAttendanceList(List<AttendanceRegisterDto> attendanceList) throws SQLException;

	boolean registerAssignment(Attendance attendance) throws SQLException;

	boolean deleteAttendance(String lectureId) throws SQLException;

	List<AttendanceResultDto> selectSugangAttendance(SugangRequestDto sugangRequestDto) throws NotFoundException, SQLException;

}
