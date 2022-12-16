package com.educator.eduo.attendence.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendence.model.dto.AttendanceResultDto;
import com.educator.eduo.attendence.model.entity.Attendance;

public interface AttendanceMapper {

	List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException;

	boolean updateAttendance(Attendance attendance) throws SQLException;

}
