package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    boolean updateTeacher(Teacher teacher);

    boolean updateAssistant(Assistant assistant);

    boolean updateStudent(Student student);

    boolean updateUser(User user) throws UsernameNotFoundException;

}
