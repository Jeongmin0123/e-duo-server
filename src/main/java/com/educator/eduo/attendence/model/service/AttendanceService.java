package com.educator.eduo.attendence.model.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendence.model.dto.AttendanceResultDto;

public interface AttendanceService {

	List<AttendanceResultDto> selectAttendanceList(String lectureId) throws NotFoundException;

}
