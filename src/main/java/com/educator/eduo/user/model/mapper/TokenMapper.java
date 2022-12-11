package com.educator.eduo.user.model.mapper;

import com.educator.eduo.user.model.entity.Token;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {

    Optional<Token> selectTokenByUserId(String userId);

    Optional<Token> selectTokenByRefreshToken(String refreshToken);

    int insertToken(Token token);

    int updateTokenByUserId(Token token);

    int deleteTokenByUserId(String userId);

}
