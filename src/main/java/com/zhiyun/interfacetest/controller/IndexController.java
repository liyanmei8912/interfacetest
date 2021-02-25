package com.zhiyun.interfacetest.controller;

import com.zhiyun.interfacetest.mapper.UserMapper;
import com.zhiyun.interfacetest.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
*Created by yanmeiLi on 2021/01/26
*
 */
@Slf4j
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String requestToken = cookie.getValue();
                    List<User> user = userMapper.findUser(null,requestToken,null,null);
                    log.info("通过cookie里token获取user数据"+user);
                    if(user != null){
                        request.getSession().setAttribute("user", user.get(0));
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
