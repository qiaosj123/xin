package com.jk.controller;

import com.jk.model.User;
import com.jk.model.User1;
import com.jk.service.UserService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AmqpTemplate amqpTemplate;

   /* @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
*/
    @Autowired
    private JestClient jestClient;

    @RequestMapping("addUser")
    @ResponseBody
    public void addUser(User user) throws IOException {
        userService.addUser(user);
        String jsonstr = JSONObject.toJSONString(user);
        Index build = new Index.Builder(user).index("book-002").type("jk-book").id(UUID.randomUUID().toString()).build();
        jestClient.execute(build);
        amqpTemplate.convertAndSend("1809a-user",jsonstr);
    }

    @RequestMapping("findUserMongo")
    @ResponseBody
    public List<User1> findUserMongo(Integer page, Integer rows,User1 user1){
       return userService.findUserMongo(page,rows,user1);
    }
    @RequestMapping("findUserMysql")
    @ResponseBody
    public HashMap<String,Object> findUserMysql(Integer page, Integer rows){
        return userService.findUserMysql(page,rows);
    }

    @RequestMapping("redis")
    public String redis(){
        System.out.println("asdasdasd");
        return   "redis";
    }
    @RequestMapping("mongodb")
    public String mongodb(){
        System.out.println("asdasdasd");
        return   "mongodb";
    }
    @RequestMapping("es")
    public String es(){
        System.out.println("asdasdasd");
        return   "es";
    }



}
