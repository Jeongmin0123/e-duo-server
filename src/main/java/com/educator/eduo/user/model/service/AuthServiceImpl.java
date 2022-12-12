package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.dto.LoginDto;
import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.Token;
import com.educator.eduo.user.model.entity.User;
import com.educator.eduo.user.model.mapper.TokenMapper;
import com.educator.eduo.user.model.mapper.UserMapper;
import com.educator.eduo.security.TokenProvider;
import com.educator.eduo.util.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.TabExpander;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
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
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final String requestUrlForKakao;

    @Autowired
    public AuthServiceImpl(
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
    public JwtResponse authenticate(LoginDto loginDto) throws SQLException {
        logger.info("Login Target: {}", loginDto);
        Authentication authentication = saveAuthentication(loginDto);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return tokenProvider.createJwtResponse(authentication, newToken);
    }

    @Override
    @Transactional
    public Object getUserInfoUsingKakao(String accessTokenByKakao) throws SQLException, JsonProcessingException {
        HttpHeaders headers = HttpUtil.generateHttpHeadersForJWT(accessTokenByKakao);
        RestTemplate restTemplate = HttpUtil.generateRestTemplate();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(requestUrlForKakao, HttpMethod.GET, request, String.class);

        JsonNode json = new ObjectMapper().readTree(response.getBody());
        String userId = json.get("kakao_account").get("email").asText();
        String pseudoPassword = UUID.randomUUID().toString();
        LoginDto loginDto = LoginDto.builder()
                                    .userId(userId)
                                    .password(passwordEncoder.encode(pseudoPassword))
                                    .build();

        try {
            return oauthLogin(loginDto);
        } catch (UsernameNotFoundException e) {
            logger.info(e.getMessage());
            loginDto.setPassword("");
            return loginDto;
        }
    }

    @Override
    @Transactional
    public JwtResponse registerUser(Map<String, Object> params)
            throws SQLException, DuplicateMemberException, IllegalArgumentException, UsernameNotFoundException {
        // 1. userId@domain 으로 아이디 중복 검사
        String userId = (String) params.get("userId");
        if (userMapper.existsByUserId(userId)) {
            throw new DuplicateMemberException("이미 가입된 회원입니다.");
        }

        // 2. ObjectMapper -> 맞는 VO로 변환 후 user 테이블과 role에 맞는 테이블에 정보 입력
        insertMultiUserInfo(params);
        User user = userMapper.selectUserByUserId(userId)
                              .orElseThrow(() -> new UsernameNotFoundException("회원 가입에 실패했습니다."));

        return saveAuthenticationDirectly(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistsUserId(String userId) throws SQLException {
        return userMapper.existsByUserId(userId);
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

    private Token registerOrUpdateJwtToken(Authentication authentication) throws SQLException {
        Optional<Token> oldToken = tokenMapper.selectTokenByUserId(authentication.getName());
        Token newToken = tokenProvider.createNewToken(authentication);

        if (oldToken.isPresent()) {
            tokenMapper.updateTokenByUserId(newToken);
        } else {
            tokenMapper.insertToken(newToken);
        }

        return newToken;
    }

    private JwtResponse saveAuthenticationDirectly(User user) throws SQLException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, "", user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token newToken = registerOrUpdateJwtToken(authentication);
        return tokenProvider.createJwtResponse(authentication, newToken);
    }

    private JwtResponse oauthLogin(LoginDto loginDto) throws SQLException, UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(loginDto.getUserId())
                              .orElseThrow(() -> new UsernameNotFoundException(loginDto.getUserId() + "는 회원이 아닙니다."));

        return saveAuthenticationDirectly(user);
    }

    private void insertMultiUserInfo(Map<String, Object> params) throws SQLException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        String roleType = (String) params.get("role");

        if (roleType.equals("ROLE_TEACHER")) {
            Teacher teacher = objectMapper.convertValue(params, Teacher.class);
            logger.info("Teacher : {}\tuserId : {}", teacher, teacher.getUserId());

            teacher.encryptPassword(passwordEncoder);
            userMapper.insertUser(teacher);
            userMapper.insertTeacher(teacher);
        }

        if (roleType.equals("ROLE_ASSISTANT")) {
            Assistant assistant = objectMapper.convertValue(params, Assistant.class);
            logger.info("Assistant : {}\tuserId : {}", assistant, assistant.getUserId());

            assistant.encryptPassword(passwordEncoder);
            userMapper.insertUser(assistant);
            userMapper.insertAssistant(assistant);
        }

        if (roleType.equals("ROLE_STUDENT")) {
            Student student = objectMapper.convertValue(params, Student.class);
            logger.info("Student : {}\tuserId : {}", student, student.getUserId());

            student.encryptPassword(passwordEncoder);
            userMapper.insertUser(student);
            userMapper.insertStudent(student);
        }

        throw new IllegalArgumentException("잘못된 ROLE이 입력되었습니다.");
    }

}
