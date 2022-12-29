package com.educator.eduo.lecture.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.lecture.model.dto.LectureAttendanceRegisterDto;
import com.educator.eduo.lecture.model.dto.LectureResultDto;
import com.educator.eduo.lecture.model.dto.TodayLectureResultDto;
import com.educator.eduo.lecture.model.entity.Lecture;

@Mapper
public interface LectureMapper {

	boolean insertLecture(Lecture lecture) throws SQLException;

	boolean updateLecture(Lecture lecture) throws SQLException;

	boolean deleteLecture(String lectureId) throws SQLException;

	List<LectureResultDto> selectAllLecture(String courseId) throws NotFoundException;

	void insertAllLecture(List<Lecture> lectureList) throws SQLException;

	List<LectureResultDto> selectSugangLecture(String courseId) throws NotFoundException;

	List<LectureAttendanceRegisterDto> selectLecture(Lecture lecture) throws SQLException;

	void modifyExistAssignment(String lectureId) throws SQLException;

	List<TodayLectureResultDto> selectTodayLecture(String userId) throws NotFoundException;
}
