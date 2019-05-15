package com.jk.controller;

import com.alibaba.fastjson.JSONObject;
import com.jk.model.User;
import com.jk.utils.HttpClientUtil;
import com.jk.utils.MD5Util;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Controller
public class UserController {
    public static final String PHONEIP="https://api.miaodiyun.com/20150822/industrySMS/sendSMS";
    @RabbitListener(queues = "1809a-user")
    public void receiveOrder(String mes) throws MessagingException {

        User user = JSONObject.parseObject(mes, User.class);
        //--------------------------发送短信-------------------------------
        HashMap<String, Object> params = new HashMap<String, Object>();


        params.put("accountSid", "a766df41026644c9b137c3a6ed51e363");
        params.put("templateid","1459487571");
        params.put("to", "17539595870");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time =  sdf.format(new Date());
        params.put("timestamp", time);
        String i ="a766df41026644c9b137c3a6ed51e363"+"54a4dcd74f8d459596d0078724d74ea1"+time;
        String md532 = MD5Util.getMd532(i);
        params.put("sig", md532);
        String yzm="您购买的XXXX已成功提交订单";
        params.put("param",yzm);

        // session.setAttribute("phoneyzm", yzm);
        String post = HttpClientUtil.post(PHONEIP, params);
        System.out.println(post);

        //-------------------发送邮件-------------------------------------
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host","smtp.163.com");// smtp服务器地址

            Session session = Session.getInstance(props);
            session.setDebug(true);

            Message msg = new MimeMessage(session);
            msg.setSubject("test");
            msg.setText("您购买的XXXX已成功提交订单");
            msg.setFrom(new InternetAddress("qsj1016795884@163.com"));//发件人邮箱
            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress("scj15054258452@163.com")); //收件人邮箱
            msg.saveChanges();

            Transport transport = session.getTransport();
            transport.connect("qsj1016795884@163.com","qsjwoaini1");//发件人邮箱,授权码

            transport.sendMessage(msg, msg.getAllRecipients());

            System.out.println("邮件发送成功...");
            transport.close();
        }

    }

