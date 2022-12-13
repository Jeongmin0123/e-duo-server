package com.educator.eduo.user.model.service;

import com.educator.eduo.user.model.entity.*;

import java.sql.SQLException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    void updatePassword(User user) throws SQLException, UsernameNotFoundException;

    void insertHire(Hire hire) throws SQLException;
}
