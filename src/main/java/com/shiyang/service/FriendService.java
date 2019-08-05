package com.shiyang.service;

import com.shiyang.pojo.TbFriendReq;
import com.shiyang.pojo.User;

import java.util.List;

/**
 * @author ShiYang
 * @title FriendService
 * @description
 * @date 2019/07/29
 */
public interface FriendService {
    /**
     * 发送添加好友请求
     *
     * @param tbFriendReq 好友请求信息
     */
    void sendRequest(TbFriendReq tbFriendReq);

    /**
     * 根据用户id,查询该用户的好友请求
     *
     * @param userid 用户id
     * @return java.util.List<com.shiyang.pojo.TbFriendReq>
     */
    List<User> findFriendReq(String userid);

    /**
     * 接受好友请求
     *
     * @param reqid 好友请求关系的id主键
     */
    void acceptFriendReq(String reqid);

    /**
     * 忽略好友请求
     *
     * @param reqid 好友请求关系的id主键
     */
    void ignoreFriendReq(String reqid);

    List<User> findFriendByUserid(String userid);
}
