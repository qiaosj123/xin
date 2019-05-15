package com.jk.model;


import java.io.Serializable;

/**
 * 类描述：
 * 创建人：乔世杰
 * 创建时间：2019/5/215:51
 * 修改人：乔世杰
 * 修改时间：2019/5/215:51
 * 修改备注：
 */

public class User implements Serializable {

    private static final long serialVersionUID = 8741649862497029413L;
    private Integer id;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
