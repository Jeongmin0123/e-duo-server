package com.educator.eduo.sugang.model.mapper;

import java.sql.SQLException;

import com.educator.eduo.sugang.model.dto.SugangRequestDto;

public interface SugangMapper {

	boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException;

}
