package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.Past;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value={"password", "ssn"})
//@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Entity
/*@SequenceGenerator(
        name="USER_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="USER_SEQ", //시퀀스 이름
        initialValue=100, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
) */
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USE_SEQ_GEN")
    @GeneratedValue
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
    @ApiModelProperty(notes="사용자 이름을 입력해 주세요.")
    private String name;
    @Past(message = "미래일자는 가입일자 일 수 없습니다.")
    @ApiModelProperty(notes="사용자 등록일을 입력해 주세요.")
    private Date joinDate;

    //@JsonIgnore
    @ApiModelProperty(notes="사용자 비밀번호를 입력해 주세요.")
    private String password;
    //@JsonIgnore
    private String ssn;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User(int id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
