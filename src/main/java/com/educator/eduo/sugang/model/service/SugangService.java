package com.educator.eduo.sugang.model.service;

import java.sql.SQLException;

import com.educator.eduo.sugang.model.dto.SugangRequestDto;

public interface SugangService {

	boolean insertSugang(SugangRequestDto sugangRequestDto) throws SQLException;

}
