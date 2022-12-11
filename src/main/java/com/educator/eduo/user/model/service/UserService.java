package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;

public interface UserService {
    int updateTeacher(Teacher teacher);

    int updateAssistant(Assistant assistant);

    int updateStudent(Student student);
}
