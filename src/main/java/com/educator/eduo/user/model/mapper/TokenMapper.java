package com.educator.eduo.user.model.mapper;

import com.educator.eduo.user.model.entity.Token;
import java.sql.SQLException;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {

    Optional<Token> selectTokenByUserId(String userId) throws SQLException;

    int updateTokenByUserId(Token token) throws SQLException;

    int insertToken(Token token) throws SQLException;

}
