package com.zhiyun.interfacetest.model;

import lombok.Data;

/*
 *Created by yanmeiLi on 2021/02/09
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
