package com.educator.eduo.lecture.model.service;

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
import com.educator.eduo.attendance.model.entity.Attendance;
import com.educator.eduo.attendance.model.mapper.AttendanceMapper;
import com.educator.eduo.lecture.model.dto.LectureAttendanceRegisterDto;
import com.educator.eduo.lecture.model.dto.LectureResultDto;
import com.educator.eduo.lecture.model.entity.Lecture;
import com.educator.eduo.lecture.model.mapper.LectureMapper;

@Service
public class LectureServiceImpl implements LectureService {

	Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);
	
	private final LectureMapper lectureMapper;
	private final AttendanceMapper attendanceMapper;
	
	@Autowired
	public LectureServiceImpl(LectureMapper lectureMapper, AttendanceMapper attendanceMapper) {
		this.lectureMapper = lectureMapper;
		this.attendanceMapper = attendanceMapper;
	}
	
	@Override
	@Transactional
	public boolean insertLecture(Lecture lecture) throws SQLException {
		if(lectureMapper.insertLecture(lecture)) {
			List<AttendanceRegisterDto> inputAttendanceList = new ArrayList<>();
			List<LectureAttendanceRegisterDto> attendanceList = lectureMapper.selectLecture(lecture);
			for(int i = 0 ; i < attendanceList.size() ; i++) {
				LectureAttendanceRegisterDto registerAttendance = attendanceList.get(i);
				AttendanceRegisterDto attendance = null;
				if(registerAttendance.isExistAssignment()) {
					attendance = AttendanceRegisterDto.builder().
						userId(registerAttendance.getUserId()).lectureId(registerAttendance.getLectureId()).assignment(1).build();
				} else {
					attendance = AttendanceRegisterDto.builder().
							userId(registerAttendance.getUserId()).lectureId(registerAttendance.getLectureId()).assignment(0).build();
				}
				inputAttendanceList.add(attendance);
				attendanceMapper.insertAttendanceList(inputAttendanceList);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateLecture(Lecture lecture) throws SQLException {
		return lectureMapper.updateLecture(lecture);
	}

	@Override
	@Transactional
	public boolean deleteLecture(String lectureId) throws SQLException {
		if(attendanceMapper.deleteAttendance(lectureId)) {
			if(lectureMapper.deleteLecture(lectureId)) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public List<LectureResultDto> selectAllLecture(String courseId) throws NotFoundException {
		List<LectureResultDto> lectureResultList = lectureMapper.selectAllLecture(courseId);
		if(lectureResultList.isEmpty()) throw new NotFoundException("해당 내용이 존재하지 않습니다.");
		return lectureResultList;
	}

}
