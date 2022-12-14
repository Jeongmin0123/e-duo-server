package com.educator.eduo.lecture.model.service;

import java.sql.SQLException;

import com.educator.eduo.lecture.model.entity.Lecture;

public interface LectureService {

	boolean insertLecture(Lecture lecture) throws SQLException;

	boolean updateLecture(Lecture lecture) throws SQLException;

	boolean deleteLecture(String lectureId) throws SQLException;

}
