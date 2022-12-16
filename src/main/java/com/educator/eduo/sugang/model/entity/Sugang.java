package com.educator.eduo.sugang.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sugang {
	private String userId;
	private String courseId;
	private String startDate;
	private String endDate;
	private String state;
}
