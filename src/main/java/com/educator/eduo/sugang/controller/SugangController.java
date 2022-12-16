package com.educator.eduo.sugang.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.educator.eduo.sugang.model.dto.SugangRequestDto;
import com.educator.eduo.sugang.model.service.SugangService;

@RestController
@CrossOrigin("*")
public class SugangController {
	
	Logger logger = LoggerFactory.getLogger(SugangController.class);
	
	private final SugangService sugangService;
	
	public SugangController(SugangService sugangService) {
		this.sugangService = sugangService;
	}
	
	@PostMapping("/api/sugang")
	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<?> insertSugang(@RequestBody SugangRequestDto sugangRequestDto) throws SQLException {
		if(!sugangService.insertSugang(sugangRequestDto)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}
