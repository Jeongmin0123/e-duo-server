package com.educator.eduo.auth.model.entity;

import lombok.Data;

//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
public class Student extends User {

    private String birthDate;
    private String schoolName;
    private int grade;
    private String parent;
    private String parentPhone;

}
