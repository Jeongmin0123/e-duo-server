package com.educator.eduo.user.model.mapper;

import com.educator.eduo.user.model.entity.Token;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {

    Optional<Token> selectTokenByUserId(String userId);

    int updateTokenByUserId(Token token);

    int insertToken(Token token);

}
