package com.educator.eduo.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
public class Student  extends User{
    private String birthDate;
    private String schoolName;
    private int grade;
    private String parent;
    private String parentPhone;
}
