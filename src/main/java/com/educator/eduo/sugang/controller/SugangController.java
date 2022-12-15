package com.educator.eduo.sugang.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.educator.eduo.sugang.model.service.SugangService;

@RestController
@CrossOrigin("*")
public class SugangController {
	
	Logger logger = LoggerFactory.getLogger(SugangController.class);
	
	private final SugangService sugangService;
	
	public SugangController(SugangService sugangService) {
		this.sugangService = sugangService;
	}
	
	
}
