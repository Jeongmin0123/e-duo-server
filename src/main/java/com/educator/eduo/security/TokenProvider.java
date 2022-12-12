package com.educator.eduo.security;

import com.educator.eduo.user.model.dto.JwtResponse;
import com.educator.eduo.user.model.entity.Token;
import com.educator.eduo.user.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Component
public class TokenProvider implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_CLAIM_KEY = "auth";
    private static final String NAME_CLAIM_KEY = "name";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    private final String secret;
    private final long accessTokenValidityInMilliSeconds;
    private final long refreshTokenValidityInMilliSeconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accesstoken-validity-in-seconds}") long accessTokenValidityInMilliSeconds,
            @Value("${jwt.refreshtoken-validity-in-seconds}") long refreshTokenValidityInMilliSeconds
    ) {
        this.secret = secret;
        this.accessTokenValidityInMilliSeconds = accessTokenValidityInMilliSeconds;
        this.refreshTokenValidityInMilliSeconds = refreshTokenValidityInMilliSeconds;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, this.accessTokenValidityInMilliSeconds);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, this.refreshTokenValidityInMilliSeconds);
    }

    public Token createNewToken(Authentication authentication) {
        return new Token(
                authentication.getName(),
                createAccessToken(authentication),
                createRefreshToken(authentication)
        );
    }

    public boolean validateToken(String token) {
        try {
            logger.info("Validation Check Token : {}", token);
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("잘못된 JWT 토큰입니다.");
        }

        return false;
    }

    public JwtResponse createJwtResponse(Authentication authentication, Token token) {
        User user = (User) authentication.getPrincipal();

        return JwtResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userId(user.getUserId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_KEY);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token).getBody();

        User principal = User.builder()
                             .userId(claims.getSubject())
                             .name((String) claims.get(NAME_CLAIM_KEY))
                             .role((String) claims.get(AUTHORITIES_CLAIM_KEY))
                             .build();

        Collection<? extends GrantedAuthority> authorities = getAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private String createToken(Authentication authentication, long validityTime) {
        User user = (User) authentication.getPrincipal();
        String authorities = getAuthorities(user);
        logger.info("authorities of token: {}", authorities);

        Date expirationTime = new Date(System.currentTimeMillis() + validityTime);
        logger.info("expiration time of token: {}", expirationTime);

        return Jwts.builder()
                   .setSubject(user.getUserId())
                   .claim(AUTHORITIES_CLAIM_KEY, authorities)
                   .claim(NAME_CLAIM_KEY, user.getName())
                   .setExpiration(expirationTime)
                   .signWith(this.key, SignatureAlgorithm.HS512)
                   .compact();
    }

    private String getAuthorities(User user) {
        return user.getAuthorities()
                   .stream()
                   .map(GrantedAuthority::getAuthority)
                   .collect(Collectors.joining(","));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        String[] authorities = claims.get(AUTHORITIES_CLAIM_KEY)
                                     .toString()
                                     .split(",");

        return Arrays.stream(authorities)
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }

    private Jws<Claims> getClaims(String token)
            throws SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                   .setSigningKey(this.key)
                   .build()
                   .parseClaimsJws(token);
    }

}
