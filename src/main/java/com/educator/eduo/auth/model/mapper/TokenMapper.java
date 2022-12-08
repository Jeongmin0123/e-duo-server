package com.educator.eduo.auth.model.mapper;

import com.educator.eduo.auth.model.entity.Token;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {

    Optional<Token> selectTokenByUserId(String userId);

    int insertToken(Token token);

}
