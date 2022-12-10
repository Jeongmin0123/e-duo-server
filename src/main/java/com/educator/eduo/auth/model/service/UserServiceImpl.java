package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.entity.*;
import com.educator.eduo.auth.model.mapper.TokenMapper;
import com.educator.eduo.auth.model.mapper.UserMapper;
import com.educator.eduo.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public JwtResponse authenticate(LoginDto loginDto) {
        logger.info("Login Target: {}", loginDto);
        Authentication authentication = saveAuthentication(loginDto);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return tokenProvider.createJwtResponse(authentication, newToken);
    }

    @Override
    @Transactional
    public JwtResponse oauthLogin(LoginDto loginDto) {
        User user = userMapper.loadUserByUsername(loginDto.getUserId()).orElse(null);
        if(user != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, "", user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Token newToken = registerOrUpdateJwtToken(authentication);
            return tokenProvider.createJwtResponse(authentication, newToken);
        }
        return null;
    }

    @Override
    @Transactional
    public int registerUser(Map<String, Object> params) {
        // 1. userId@domain 으로 아이디 중복 검사
        String userId= (String) params.get("userId");
        if(userMapper.selectUserByEmail(userId).isPresent()) return -1;

        // 2. ObjectMapper -> 맞는 VO로 변환 후 user 테이블과 role에 맞는 테이블에 정보 입력
        int result = insertMultiUserInfo(params);


        return result;
    }

    private Authentication saveAuthentication(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUserId(), loginDto.getPassword()
        );
        logger.info("로그인 성공");
        // 메서드 authenticate()에서 UserDetailsServiceImpl의 loadUserByUserName을 호출하고, 최종적으로 Authentication을 만들어준다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Save Authenticate In Context: {}", authentication);

        return authentication;
    }

    private Token registerOrUpdateJwtToken(Authentication authentication) {
        Optional<Token> oldToken = tokenMapper.selectTokenByUserId(authentication.getName());
        Token newToken = tokenProvider.createNewToken(authentication);

        if (oldToken.isPresent()) {
            tokenMapper.updateTokenByUserId(newToken);
        } else {
            tokenMapper.insertToken(newToken);
        }

        return newToken;
    }

    private int insertMultiUserInfo(Map<String, Object> params) {
        int result = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        String roleType = (String) params.get("role");
        if(roleType.equals("ROLE_TEACHER")) {
            Teacher teacher = objectMapper.convertValue(params, Teacher.class);
            logger.info("Teacher : {}\tuserId : {}", teacher, teacher.getUserId());
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
            userMapper.insertUser(teacher);
            result = userMapper.insertTeacher(teacher);
        } else if (roleType.equals("ROLE_ASSISTANT")) {
            Assistant assistant = objectMapper.convertValue(params, Assistant.class);
            logger.info("Assistant : {}\tuserId : {}", assistant, assistant.getUserId());
            assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
            userMapper.insertUser(assistant);
            result = userMapper.insertAssistant(assistant);
        } else if (roleType.equals("ROLE_STUDENT")) {
            Student student = objectMapper.convertValue(params, Student.class);
            logger.info("Student : {}\tuserId : {}", student, student.getUserId());
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            userMapper.insertUser(student);
            result = userMapper.insertStudent(student);
        }
        return result;
    }
}
