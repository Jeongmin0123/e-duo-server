package com.educator.eduo.sugang.model.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.educator.eduo.sugang.model.dto.SugangRequestDto;
import com.educator.eduo.sugang.model.mapper.SugangMapper;

public class SugangServiceImpl implements SugangService {

Logger logger = LoggerFactory.getLogger(SugangServiceImpl.class);
	
	private final SugangMapper sugangMapper;
	
	@Autowired
	public SugangServiceImpl(SugangMapper sugangMapper) {
		this.sugangMapper = sugangMapper;
	}
	
	@Override
	public boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException {
		return sugangMapper.insertSugang(sugangRequestDto);
	}

}
