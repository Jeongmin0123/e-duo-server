package com.educator.eduo.attendence.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResultDto {
	private String attendanceId;
	private String lectureId;
	private String studentId;
	private String studentName;
	private String assignment;
	private String doneDate;
	private String testScore;
	private String checkIn;
}
