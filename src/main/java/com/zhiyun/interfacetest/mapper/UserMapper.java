package com.zhiyun.interfacetest.mapper;

import com.zhiyun.interfacetest.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
 *Created by yanmeiLi on 2021/02/09
 */
@Mapper
public interface UserMapper {

    @Select("<script> " +
            "select * from user " +
            "<where>" +
            "<if test='accountId != null'>"+
            "and account_id = #{accountId}"+
            "</if>"+
            "<if test='token != null'>"+
            "and token = #{token}"+
            "</if>"+
            "<if test='name != null'>"+
            "and name = #{name}"+
            "</if>"+
            "<if test='id != null'>"+
            "and id = #{id}"+
            "</if>"+
            "</where>"+
            "</script>"
    )
    @Results(
            id="userMap", value={
            @Result(property = "accountId",column = "account_id"),
            @Result(property = "gmtCreate",column = "gmt_create"),
            @Result(property = "gmtModified",column = "gmt_modified")
            }
        )
    List<User> findUser(@Param("accountId") String accountId, @Param("token") String token, @Param("name") String name, @Param("id") Integer id);

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

//    @Select("select * from user where account_id = #{accountId}")
//    @ResultMap("userMap")
//    List<User> findUserByAccountId(@Param("accountId") String accountId);
}
