package com.educator.eduo.sugang.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
	public ResponseEntity<?> insertSugang(@RequestBody SugangRequestDto sugangRequestDto) throws SQLException {
		if(!sugangService.insertSugang(sugangRequestDto)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping("/api/sugang")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<?> confirmSugang(@RequestBody SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException {
		sugangService.confirmSugang(sugangRequestDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/api/sugang/giveUp")
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<?> giveUpSugang(@RequestBody SugangRequestDto sugangRequestDto) throws SQLException, NotFoundException {
		sugangService.giveUpSugang(sugangRequestDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
