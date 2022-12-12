package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.User;
import java.sql.SQLException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    boolean updateTeacher(Teacher teacher) throws SQLException;

    boolean updateAssistant(Assistant assistant) throws SQLException;

    boolean updateStudent(Student student) throws SQLException;

    void updatePassword(User user) throws SQLException, UsernameNotFoundException;

}
