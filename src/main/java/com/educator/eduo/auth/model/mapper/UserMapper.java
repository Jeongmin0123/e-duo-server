package com.educator.eduo.auth.model.mapper;

import com.educator.eduo.auth.model.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> selectOneByUserId(String userId);
}
