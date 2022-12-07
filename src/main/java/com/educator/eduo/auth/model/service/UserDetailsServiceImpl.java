package com.educator.eduo.auth.model.service;

import com.educator.eduo.auth.model.mapper.UserMapper;
import com.educator.eduo.auth.model.vo.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOneByUserId(username)
                .orElseThrow(() ->  new UsernameNotFoundException(username + " DB에서 찾을 수 없습니다."));
        logger.info("loadUserByUsername -> {}", user);
        logger.info("authorities는?? {}", user.getAuthorities());

        return user;
    }
}
