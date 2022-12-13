package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.*;
import com.educator.eduo.user.model.mapper.UserMapper;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void insertHire(Hire hire) throws SQLException {
        userMapper.insertHire(hire);
        return;
    }
}
