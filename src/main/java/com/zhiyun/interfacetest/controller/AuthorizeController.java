package com.zhiyun.interfacetest.controller;

import com.alibaba.fastjson.JSON;
import com.zhiyun.interfacetest.dto.AccessTokenDTO;
import com.zhiyun.interfacetest.dto.GithubUserDTO;
import com.zhiyun.interfacetest.mapper.UserMapper;
import com.zhiyun.interfacetest.model.User;
import com.zhiyun.interfacetest.provider.GithubProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/*
*Created by yanmeiLi on 2021/01/27
 */
@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @RequestMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response)
    {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String resBody = githubProvider.getAccessToken(accessTokenDTO);
        String accessToken = resBody.split("&")[0].split("=")[1];
        log.info("accessToken的值是"+accessToken);
        GithubUserDTO githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null){
            String accounId = String.valueOf(githubUser.getId());
            List<User> users = userMapper.findUser(accounId,null,null,null);
            log.info("users的值为"+users);
            if(users == null) {
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setName(githubUser.getName());
                user.setToken(token);
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
                response.addCookie(new Cookie("token", token));
            }else{
                String token = users.get(0).getToken();
                response.addCookie(new Cookie("token", token));
            }
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
}
