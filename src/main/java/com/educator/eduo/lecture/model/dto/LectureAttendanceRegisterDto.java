package com.educator.eduo.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureAttendanceRegisterDto {
	private String lectureId;
	private String userId;
	private boolean existAssignment;
}
