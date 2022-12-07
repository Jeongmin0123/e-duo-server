package com.educator.eduo.security.jwt;

import com.educator.eduo.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
//    headers: {
//        "Authorization": "Bearer toeknsaljlkbdncldjfnd-0fknldsjbsd"
//    }


    // filterChain 내 순서가 됐을 때 이 로직을 수행하라
    // 끝나면 filterChain.doFilter();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        // 토큰이 존재 && 유효하다면
        if(jwt != null && tokenProvider.validateToken(jwt)) {
            logger.info("JwtFilet with Token : {}", jwt);
            // Authentication 가져오기
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // Context에 authentication 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Security Context에 {} 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        }
        filterChain.doFilter(request, response);
    }

    // Header에서 토큰 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
