package com.educator.eduo.auth.model.mapper;

import com.educator.eduo.auth.model.entity.User;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> selectUserByEmail(Map<String, String> emailMap);

}
