package com.educator.eduo.lecture.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.educator.eduo.lecture.model.entity.Lecture;

@Mapper
public interface LectureMapper {

	boolean insertLecture(Lecture lecture) throws SQLException;

	boolean updateLecture(Lecture lecture) throws SQLException;

	boolean deleteLecture(String lectureId) throws SQLException;

}
