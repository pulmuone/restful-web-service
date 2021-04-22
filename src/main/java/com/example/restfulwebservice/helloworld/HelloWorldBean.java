package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lombok
@Data
@AllArgsConstructor //필드를 기본 파라미터로 갖는 생성자를 자동으로 만든다.
@NoArgsConstructor //기본 생성자
public class HelloWorldBean {
    private String message;
}
