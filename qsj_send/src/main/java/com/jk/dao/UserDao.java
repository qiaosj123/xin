package com.jk.dao;

import com.jk.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    @Insert("insert into t_user(name,phone,age,email) values(#{name},#{phone},#{age},#{email})")
    void addUser(User user);

    @Select("select * from t_user limit #{page},#{rows}")
    List<User> findUserMysql(Integer page, Integer rows);

    @Select("select count(1) from t_user")
    Integer getCount();
}
