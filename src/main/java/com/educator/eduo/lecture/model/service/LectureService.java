package com.educator.eduo.lecture.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;

import com.educator.eduo.lecture.model.dto.LectureResultDto;
import com.educator.eduo.lecture.model.dto.TodayLectureResultDto;
import com.educator.eduo.lecture.model.entity.Lecture;

public interface LectureService {

	boolean insertLecture(Lecture lecture) throws SQLException;

	boolean updateLecture(Lecture lecture) throws SQLException;

	boolean deleteLecture(String lectureId) throws SQLException;

	List<LectureResultDto> selectAllLecture(String courseId) throws NotFoundException;

	List<TodayLectureResultDto> selectTodayLecture(String userId) throws NotFoundException;

}
