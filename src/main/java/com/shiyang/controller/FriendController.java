package com.shiyang.controller;

import com.shiyang.pojo.Result;
import com.shiyang.pojo.TbFriend;
import com.shiyang.pojo.TbFriendReq;
import com.shiyang.pojo.User;
import com.shiyang.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShiYang
 * @title FriendController
 * @description 好友控制
 * @date 2019/07/29
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @RequestMapping("/sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq tbFriendReq) {
        try {
            friendService.sendRequest(tbFriendReq);
            return new Result(true, "发送请求成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送请求失败");
        }
    }

    @RequestMapping("/findFriendReqByUserid")
    public List<User> findFriendReq(String userid) {
        return friendService.findFriendReq(userid);
    }

    @RequestMapping("/acceptFriendReq")
    public Result acceptFriendReq(String reqid) {
        try {
            friendService.acceptFriendReq(reqid);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/ignoreFriendReq")
    public Result ignoreFriendReq(String reqid) {
        try {
            friendService.ignoreFriendReq(reqid);
            return new Result(true, "忽略成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "忽略失败");
        }
    }

    @RequestMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid) {
        try {
            return friendService.findFriendByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
}
