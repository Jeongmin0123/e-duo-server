package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.User;
import com.educator.eduo.user.model.mapper.UserMapper;
import java.util.Optional;
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
    public boolean updateUser(User user) {
        User searchResult = userMapper.selectUserByUserId(user.getUserId()).orElse(null);

        if(searchResult == null) return 0;
        searchResult.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.updateUser(searchResult);
    }

    @Override
    @Transactional
    public boolean updateTeacher(Teacher teacher) {
        teacher.encryptPassword(passwordEncoder);
        userMapper.updateUser(teacher);
        return userMapper.updateTeacher(teacher) == 1;
    }

    @Override
    @Transactional
    public boolean updateAssistant(Assistant assistant) {
        assistant.encryptPassword(passwordEncoder);
        userMapper.updateUser(assistant);
        return userMapper.updateAssistant(assistant) == 1;
    }

    @Override
    @Transactional
    public boolean updateStudent(Student student) {
        student.encryptPassword(passwordEncoder);
        userMapper.updateUser(student);
        return userMapper.updateStudent(student) == 1;
    }

}
