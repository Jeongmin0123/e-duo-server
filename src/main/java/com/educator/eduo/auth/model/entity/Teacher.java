package com.educator.eduo.auth.model.entity;

import lombok.Data;

//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
public class Teacher extends User {

    private String subject;
    private String imageSrc;

}
