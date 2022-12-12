package com.educator.eduo.user.model.mapper;

import com.educator.eduo.user.model.entity.*;

import java.sql.SQLException;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> selectUserByUserId(String userId) throws SQLException;

    Optional<User> loadUserByUsername(String userId);

    boolean existsByUserId(String userId) throws SQLException;

    void insertUser(User user) throws SQLException;

    int insertTeacher(Teacher teacher) throws SQLException;

    int insertAssistant(Assistant assistant) throws SQLException;

    int insertStudent(Student student) throws SQLException;

    int updateUser(User user) throws SQLException;

    int updateTeacher(Teacher teacher) throws SQLException;

    int updateAssistant(Assistant assistant) throws SQLException;

    int updateStudent(Student student) throws SQLException;

    void insertHire(Hire hire);
}
