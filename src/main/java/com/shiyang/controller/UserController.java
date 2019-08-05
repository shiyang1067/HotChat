package com.shiyang.controller;

import com.shiyang.pojo.Result;
import com.shiyang.pojo.TbUser;
import com.shiyang.pojo.User;
import com.shiyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @title: UserController
 * @description: 用户控制
 * @author: ShiYang
 * @date: 2019/07/16
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public List<TbUser> findAll() {
        return userService.findAll();
    }

    @RequestMapping("/login")
    public Result login(@RequestBody TbUser tbUser) {
        try {
            User user = userService.login(tbUser.getUsername(), tbUser.getPassword());
            if (user == null) {
                return new Result(false, "登录失败,请检查用户名或者密码是否正确");
            } else {
                return new Result(true, "登录成功", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "登录失败");
        }
    }

    @RequestMapping("/register")
    public Result register(@RequestBody TbUser tbUser) {
        try {
            userService.register(tbUser);
            return new Result(true, "注册成功!");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile file, String userid) {
        try {
            User user = userService.upload(file, userid);
            if (user != null) {
                return new Result(true, "上传成功!", user);
            } else {
                return new Result(false, "上传失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败!");
        }
    }

    @RequestMapping("/updateNickname")
    public Result updateNickname(@RequestBody TbUser tbUser) {
        try {
            userService.updateNickname(tbUser.getId(), tbUser.getNickname());
            return new Result(true, "更新成功!");

        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败!");
        }
    }

    @RequestMapping("/findById")
    public User findById(String userid) {
        return userService.findById(userid);
    }

    @RequestMapping("/findByUsername")
    public Result findByUsername(String userid, String friendUsername) {
        try {
            System.out.println(userid);
            User user = userService.findByUsername(userid, friendUsername);
            if (user != null) {
                return new Result(true, "搜索成功", user);
            } else {
                return new Result(false, "没有找到该用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "搜索失败");
        }
    }
}
