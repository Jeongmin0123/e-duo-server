package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.entity.Assistant;
import com.educator.eduo.auth.model.entity.Student;
import com.educator.eduo.auth.model.entity.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

public interface UserService {

    JwtResponse authenticate(LoginDto loginDto);

    Object getUserInfoUsingKakao(String accessTokenByKakao) throws JsonProcessingException;

    Object registerUser(Map<String, Object> params);

    int updateTeacher(Teacher teacher);

    int updateAssistant(Assistant assistant);

    int updateStudent(Student student);
}
