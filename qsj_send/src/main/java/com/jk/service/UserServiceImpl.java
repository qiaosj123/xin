package com.jk.service;

import com.jk.dao.UserDao;
import com.jk.model.User;
import com.jk.model.User1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

   @Autowired
   private UserDao userDao;

   @Autowired
   RedisTemplate redisTemplate;

   @Autowired
    MongoTemplate mongoTemplate;

   //@Autowired
   // UserRepostory userRepostory;
    public void addUser(User user) {
        //存mysql
        userDao.addUser(user);
        //存redis
        //redisTemplate.opsForList().leftPush("user",user);
        //存mongo
        User1 user1 = new User1();
        BeanUtils.copyProperties(user,user1);
        mongoTemplate.insert(user1);

    }

    public List<User1> findUserMongo(Integer page, Integer rows,User1 user1) {
        Query query=new Query();
        if( user1.getPhone() != null && user1.getPhone() != "") {
            Criteria regex = Criteria.where("phone").is(user1.getPhone());
            query.addCriteria(regex);
        }
        query.skip(rows * (page - 1)).limit(rows);
        return mongoTemplate.find(query, User1.class);
    }

    public HashMap<String,Object> findUserMysql(Integer page, Integer rows) {
        HashMap<String,Object> map=new HashMap<String, Object>();
        Integer count;
         count = (Integer) redisTemplate.opsForValue().get("count");
        if (count==null){
            count=userDao.getCount();
            redisTemplate.opsForValue().set("count",count);
        }
        map.put("total",count);
        List<User> list = new ArrayList<User>();
        Integer start=(page-1)*rows;
         list=((List<User>) redisTemplate.opsForValue().get("findUserMysql" + page + "" + rows)) ;
         if (list==null||(list.size()<=0)){
             list = userDao.findUserMysql(start, rows);
             redisTemplate.opsForValue().set("findUserMysql" + page + "" + rows,list);
         }
        map.put("rows",list);
        return map;
    }
}
