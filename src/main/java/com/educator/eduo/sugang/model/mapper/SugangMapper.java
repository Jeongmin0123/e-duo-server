package com.educator.eduo.sugang.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.educator.eduo.sugang.model.dto.SugangRequestDto;

@Mapper
public interface SugangMapper {

	boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException;

	void confirmSugang(SugangRequestDto sugangRequestDto) throws SQLException;

	void giveUpSugang(SugangRequestDto sugangRequestDto) throws SQLException;
	
}
