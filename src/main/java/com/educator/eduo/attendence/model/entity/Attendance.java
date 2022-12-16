package com.educator.eduo.attendence.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
	private String attendenceId;
	private String userId;
	private String lectureId;
	private String assignment;
	private String doneDate;
	private String checkIn;
	private String testScore;
}
