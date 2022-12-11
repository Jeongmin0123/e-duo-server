package com.educator.eduo.user.controller;

import com.educator.eduo.user.model.entity.Assistant;
import com.educator.eduo.user.model.entity.Student;
import com.educator.eduo.user.model.entity.Teacher;
import com.educator.eduo.user.model.service.AuthService;
import com.educator.eduo.user.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/teacher")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> modifyTeacher(@RequestBody Teacher teacher) {
        int result = userService.updateTeacher(teacher);
        if(result != 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 실패
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/api/assistant")
    @PreAuthorize("hasRole('ROLE_ASSISTANT')")
    public ResponseEntity<?> modifyAssistant(@RequestBody Assistant assistant) {
        int result = userService.updateAssistant(assistant);
        if(result != 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 실패
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/api/student")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> modifyStudent(@RequestBody Student student) {
        int result = userService.updateStudent(student);
        if(result != 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 실패
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
