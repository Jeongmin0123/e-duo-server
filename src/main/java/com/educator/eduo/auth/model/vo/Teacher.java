package com.educator.eduo.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
public class Teacher extends User{
    private String subject;
    private String imageSrc;
}
