package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.User;
import com.educator.eduo.user.model.mapper.UserMapper;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void updatePassword(User user) throws SQLException {
        User selected = userMapper.selectUserByUserId(user.getUserId())
                                  .orElseThrow(() -> new UsernameNotFoundException("회원 가입하지 않은 유저입니다."));

        selected.setPassword(user.getPassword());
        selected.encryptPassword(passwordEncoder);
        userMapper.updateUser(selected);
    }

    @Override
    @Transactional
    public boolean updateTeacher(Teacher teacher) throws SQLException {
        teacher.encryptPassword(passwordEncoder);
        return userMapper.updateUser(teacher) == 1
                && userMapper.updateTeacher(teacher) == 1;
    }

    @Override
    @Transactional
    public boolean updateAssistant(Assistant assistant) throws SQLException {
        assistant.encryptPassword(passwordEncoder);
        return userMapper.updateUser(assistant) == 1
                && userMapper.updateAssistant(assistant) == 1;
    }

    @Override
    @Transactional
    public boolean updateStudent(Student student) throws SQLException {
        student.encryptPassword(passwordEncoder);
        return userMapper.updateUser(student) == 1
                && userMapper.updateStudent(student) == 1;
    }

}
