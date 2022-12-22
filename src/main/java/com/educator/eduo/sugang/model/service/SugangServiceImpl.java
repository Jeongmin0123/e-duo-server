package com.educator.eduo.sugang.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educator.eduo.attendance.model.dto.AttendanceRegisterDto;
import com.educator.eduo.attendance.model.dto.AttendanceResultDto;
import com.educator.eduo.attendance.model.mapper.AttendanceMapper;
import com.educator.eduo.lecture.model.dto.LectureResultDto;
import com.educator.eduo.lecture.model.mapper.LectureMapper;
import com.educator.eduo.sugang.model.dto.SugangRequestDto;
import com.educator.eduo.sugang.model.mapper.SugangMapper;

@Service
public class SugangServiceImpl implements SugangService {

Logger logger = LoggerFactory.getLogger(SugangServiceImpl.class);
	
	private final SugangMapper sugangMapper;
	private final AttendanceMapper attendanceMapper;
	private final LectureMapper lectureMapper;
	
	@Autowired
	public SugangServiceImpl(SugangMapper sugangMapper, AttendanceMapper attendanceMapper, LectureMapper lectureMapper) {
		this.sugangMapper = sugangMapper;
		this.attendanceMapper = attendanceMapper;
		this.lectureMapper = lectureMapper;
	}
	
	@Override
	public boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException {
		return sugangMapper.insertSugang(sugangRequestDto);
	}

	@Override
	@Transactional
	public void confirmSugang(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException {
		sugangMapper.confirmSugang(sugangRequestDto);
		List<LectureResultDto> lecturelist = lectureMapper.selectSugangLecture(sugangRequestDto.getCourseId());
		List<AttendanceRegisterDto> attendanceList = makeAttendanceList(lecturelist, sugangRequestDto.getUserId());
		for(int i = 0 ; i < attendanceList.size() ; i++) {
			System.out.println(attendanceList.get(i).getUserId());
		}
		attendanceMapper.insertAttendanceList(attendanceList);
		return;
	}
	
	@Override
	@Transactional
	public void giveUpSugang(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException {
		sugangMapper.giveUpSugang(sugangRequestDto);
		List<LectureResultDto> lecturelist = lectureMapper.selectAllLecture(sugangRequestDto.getCourseId());
		List<AttendanceRegisterDto> attendanceList = new ArrayList<>();
		for(int i = 0 ; i < lecturelist.size() ; i++) {
			LectureResultDto lecture = lecturelist.get(i);
			AttendanceRegisterDto attendance = AttendanceRegisterDto.builder().lectureId(lecture.getLectureId()).userId(sugangRequestDto.getUserId()).build();
			attendanceList.add(attendance);
		}
		attendanceMapper.deleteAttendanceList(attendanceList);
		return;
	}

	public List<AttendanceRegisterDto> makeAttendanceList(List<LectureResultDto> lecturelist, String userId) {
		List<AttendanceRegisterDto> attendanceList = new ArrayList<>();
		for(int i = 0 ; i < lecturelist.size() ; i++) {
			LectureResultDto lecture = lecturelist.get(i);
			if(lecture.isExistAssignment()) {
				attendanceList.add(buildAttendance(lecture.getLectureId(), userId, 1));
			} else {
				attendanceList.add(buildAttendance(lecture.getLectureId(), userId, 0));
			}
		}
		return attendanceList;
	}
	
	public AttendanceRegisterDto buildAttendance(String lectureId, String userId, int assignment) {
		return AttendanceRegisterDto.builder()
				.userId(userId)
				.lectureId(lectureId)
				.assignment(assignment)
				.build();
	}

	@Override
	public List<AttendanceResultDto> selectSugangAttendance(SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException {
		return attendanceMapper.selectSugangAttendance(sugangRequestDto);
	}
	
}
