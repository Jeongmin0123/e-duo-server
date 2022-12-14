package com.educator.eduo.lecture.model.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educator.eduo.lecture.model.entity.Lecture;
import com.educator.eduo.lecture.model.mapper.LectureMapper;

@Service
public class LectureServiceImpl implements LectureService {

	Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);
	
	private final LectureMapper lectureMapper;
	
	@Autowired
	public LectureServiceImpl(LectureMapper lectureMapper) {
		this.lectureMapper = lectureMapper;
	}
	
	@Override
	public boolean insertLecture(Lecture lecture) throws SQLException {
		return lectureMapper.insertLecture(lecture);
	}

	@Override
	public boolean updateLecture(Lecture lecture) throws SQLException {
		return lectureMapper.updateLecture(lecture);
	}

	@Override
	public boolean deleteLecture(String lectureId) throws SQLException {
		return lectureMapper.deleteLecture(lectureId);
	}

}
