package com.educator.eduo.attendance.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRegisterDto {
	private String userId;
	private String lectureId;
	private int assignment;
}
