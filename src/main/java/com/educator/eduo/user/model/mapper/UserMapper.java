package com.educator.eduo.user.model.mapper;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> selectUserByUserId(String userId);

    Optional<User> loadUserByUsername(String userId);

    boolean existsByUserId(String userId);

    void insertUser(User user);

    int insertTeacher(Teacher teacher);

    int insertAssistant(Assistant assistant);

    int insertStudent(Student student);

    int updateUser(User user);

    int updateTeacher(Teacher teacher);

    int updateAssistant(Assistant assistant);

    int updateStudent(Student student);

}
