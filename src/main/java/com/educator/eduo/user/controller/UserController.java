package com.educator.eduo.user.controller;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Hire;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.service.AuthService;
import com.educator.eduo.user.model.service.UserService;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/api/hire")
    @PreAuthorize("hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> applyAssistant(@RequestBody Hire hire) throws SQLException {
        userService.insertHire(hire);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/hire")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> modifyAssistant(@RequestBody Hire hire) throws SQLException {
        int result = userService.updateHire(hire);
        if(result != 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/fire")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> fireAssistant(@RequestBody Hire hire) throws SQLException {
        int result = userService.deleteHire(hire);
        if(result != 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
