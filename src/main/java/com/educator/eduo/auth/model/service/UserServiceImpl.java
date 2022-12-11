package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.dto.JwtResponse;
import com.educator.eduo.auth.model.dto.LoginDto;
import com.educator.eduo.auth.model.entity.Assistant;
import com.educator.eduo.auth.model.entity.Student;
import com.educator.eduo.auth.model.entity.Teacher;
import com.educator.eduo.auth.model.entity.Token;
import com.educator.eduo.auth.model.entity.User;
import com.educator.eduo.auth.model.mapper.TokenMapper;
import com.educator.eduo.auth.model.mapper.UserMapper;
import com.educator.eduo.security.TokenProvider;
import com.educator.eduo.util.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final String requestUrlForKakao;

    @Autowired
    public UserServiceImpl(
            UserMapper userMapper,
            TokenMapper tokenMapper,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder,
            @Value("${kakao.social.url}") String requestUrlForKakao
    ) {
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.requestUrlForKakao = requestUrlForKakao;
    }

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
    public Object getUserInfoUsingKakao(String accessTokenByKakao) throws JsonProcessingException {
        HttpHeaders headers = HttpUtil.generateHttpHeadersForJWT(accessTokenByKakao);
        RestTemplate restTemplate = HttpUtil.generateRestTemplate();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(requestUrlForKakao, HttpMethod.GET, request, String.class);

        JsonNode json = new ObjectMapper().readTree(response.getBody());
        String userId = json.get("kakao_accout").get("email").asText();
        String pseudoPassword = UUID.randomUUID().toString();
        LoginDto loginDto = LoginDto.builder()
                                    .userId(userId)
                                    .password(passwordEncoder.encode(pseudoPassword))
                                    .build();

        try {
            return oauthLogin(loginDto);
        } catch (UsernameNotFoundException e) {
            logger.info(e.getMessage());
            return loginDto;
        }
    }

    @Override
    @Transactional
    public Object registerUser(Map<String, Object> params) {
        // 1. userId@domain 으로 아이디 중복 검사
        String userId= (String) params.get("userId");
        if(userMapper.existsByUserId(userId)) return Integer.valueOf(-1);

        // 2. ObjectMapper -> 맞는 VO로 변환 후 user 테이블과 role에 맞는 테이블에 정보 입력
        return insertMultiUserInfo(params);

    }

    // >>>>>>>>>>>>>>>>>>>>>> UserService -> update


    @Override
    @Transactional
    public int updateTeacher(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        userMapper.updateUser(teacher);
        return userMapper.updateTeacher(teacher);
    }

    @Override
    @Transactional
    public int updateAssistant(Assistant assistant) {
        assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
        userMapper.updateUser(assistant);
        return userMapper.updateAssistant(assistant);
    }

    @Override
    @Transactional
    public int updateStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userMapper.updateUser(student);
        return userMapper.updateStudent(student);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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

    private JwtResponse saveDirectAuthentication(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, "", user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return tokenProvider.createJwtResponse(authentication, newToken);
    }

    private JwtResponse oauthLogin(LoginDto loginDto) {
        User user = userMapper.loadUserByUsername(loginDto.getUserId())
                              .orElseThrow(() -> new UsernameNotFoundException(loginDto.getUserId() + "는 회원이 아닙니다."));

        return saveDirectAuthentication(user);
    }



    private Object insertMultiUserInfo(Map<String, Object> params) {
        ObjectMapper objectMapper = new ObjectMapper();
        String roleType = (String) params.get("role");
        String userId = (String) params.get("userId");
        if(roleType.equals("ROLE_TEACHER")) {
            Teacher teacher = objectMapper.convertValue(params, Teacher.class);
            logger.info("Teacher : {}\tuserId : {}", teacher, teacher.getUserId());
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
            userMapper.insertUser(teacher);
            userMapper.insertTeacher(teacher);
        } else if (roleType.equals("ROLE_ASSISTANT")) {
            Assistant assistant = objectMapper.convertValue(params, Assistant.class);
            logger.info("Assistant : {}\tuserId : {}", assistant, assistant.getUserId());
            assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
            userMapper.insertUser(assistant);
            userMapper.insertAssistant(assistant);
        } else if (roleType.equals("ROLE_STUDENT")) {
            Student student = objectMapper.convertValue(params, Student.class);
            logger.info("Student : {}\tuserId : {}", student, student.getUserId());
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            userMapper.insertUser(student);
            userMapper.insertStudent(student);
        }

        User user = userMapper.loadUserByUsername(userId).orElse(null);
        if(user == null) return Integer.valueOf(0);
        return saveDirectAuthentication(user);
    }
}
