package com.educator.eduo.attendance.model.dto;

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
	private String schoolName;
	private String checkIn;
	private String studentName;
	private String assignment;
	private String doneDate;
	private String testScore;
}
