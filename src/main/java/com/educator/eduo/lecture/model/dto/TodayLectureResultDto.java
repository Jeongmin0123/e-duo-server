package com.educator.eduo.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayLectureResultDto {
	private String courseId;
	private String courseName;
	private String academyName;
	private String academyAddress;
	private String startDate;
	private String day;
	private String startTime;
	private String endTime;
}
