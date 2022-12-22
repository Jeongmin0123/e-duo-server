package com.educator.eduo.sugang.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.sugang.model.dto.SugangRequestDto;

public interface SugangService {

	boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException;

	void confirmSugang(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException;

	void giveUpSugang(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException;

	List<AttendanceResultDto> selectSugangAttendance(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException;

}
