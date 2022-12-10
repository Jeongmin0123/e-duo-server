package com.educator.eduo.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {
    private String birthDate;
    private String schoolName;
    private int grade;
    private String parent;
    private String parentPhone;
}
