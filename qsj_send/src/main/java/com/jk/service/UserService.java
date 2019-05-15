package com.jk.service;

import com.jk.model.User;
import com.jk.model.User1;

import java.util.HashMap;
import java.util.List;


public interface UserService {
    void addUser(User user);

    List<User1> findUserMongo(Integer page, Integer rows,User1 user1);

    HashMap<String,Object> findUserMysql(Integer page, Integer rows);
}
