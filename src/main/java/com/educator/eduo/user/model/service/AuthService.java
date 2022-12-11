package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.dto.LoginDto;
import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

public interface AuthService {

    JwtResponse authenticate(LoginDto loginDto);

    Object getUserInfoUsingKakao(String accessTokenByKakao) throws JsonProcessingException;

    Object registerUser(Map<String, Object> params);

    boolean isExistsUserId(String userId);

//    int updateTeacher(Teacher teacher);
//
//    int updateAssistant(Assistant assistant);
//
//    int updateStudent(Student student);
}
