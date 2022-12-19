package com.educator.eduo.attendance.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequestDto {
	private String lectureId;
	private String studentId;
	private String checkIn;
	private String assignment;
	private String testScore;
}
