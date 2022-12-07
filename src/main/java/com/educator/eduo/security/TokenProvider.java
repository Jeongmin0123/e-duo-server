package com.educator.eduo.security;

/*
* 토큰 생성, 조회, 유효성 검사
*
* 토큰 만들기
* - Signature -> 서명 - SecretKey
*
* */

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long accesstokenValidityInMilliSeconds;
    private final long refreshtokenValidityInMilliSeconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accesstoken-validity-in-seconds}") long accesstokenValidityInMilliSeconds,
            @Value("${jwt.refreshtoken-validity-in-seconds}") long refreshtokenValidityInMilliSeconds
    ) {
        this.secret = secret;
        this.accesstokenValidityInMilliSeconds = accesstokenValidityInMilliSeconds;
        this.refreshtokenValidityInMilliSeconds = refreshtokenValidityInMilliSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createAccessToken(Authentication authentication) {
        // Authority
//        authentication.getPrincipal() => User 객체로 담기도 한다.
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        logger.info("Create Access Token ", authorities);

        // 만료 시간
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accesstokenValidityInMilliSeconds);

        // header.payload.signature
        return Jwts.builder()
                // Security 인증 인가  -> 접근주체 Principal = username==userId
                .setSubject(authentication.getName()) // userId
                .claim(AUTHORITIES_KEY, authorities) // auth: roles
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        // Authority
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        System.out.println(authorities);

        // set expiration time
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshtokenValidityInMilliSeconds);

        return Jwts.builder()
                .setSubject(authentication.getName()) // username
                // claim에 key="auth" , data = username 을 넣기도 한다
                .claim(AUTHORITIES_KEY, authorities) // Key, roles
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        // authentication 만들어 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // Token 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch(ExpiredJwtException e ) {
            logger.info("만료된 토큰입니다.");
        } catch(UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch(IllegalArgumentException e) {
            logger.info("잘못된 JWT 토큰입니다.");
        }
        return false;
    }
}
