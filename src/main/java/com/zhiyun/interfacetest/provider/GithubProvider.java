package com.zhiyun.interfacetest.provider;

import com.alibaba.fastjson.JSON;
import com.zhiyun.interfacetest.dto.AccessTokenDTO;
import com.zhiyun.interfacetest.dto.GithubUserDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

/*
 *Created by yanmeiLi on 2021/01/27
 *
 */
@Component
@Slf4j
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        String url = "https://github.com/login/oauth/access_token";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String bodyString = response.body().string();
            log.info("bodyString的值是"+bodyString);
            return bodyString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GithubUserDTO getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization","token "+accessToken)
                .build();
        log.info("request的值是"+request);
        try {
            Response response = client.newCall(request).execute();
            String resBody = response.body().string();
            log.info("resBody的值是"+resBody);
            GithubUserDTO githubUserDTO = JSON.parseObject(resBody, GithubUserDTO.class);
            log.info("githubUserDTO的值是"+githubUserDTO);
            return githubUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
