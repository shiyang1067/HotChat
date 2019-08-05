package com.shiyang.service.impl;

import com.shiyang.utils.IdWorker;
import com.shiyang.mapper.TbFriendMapper;
import com.shiyang.mapper.TbFriendReqMapper;
import com.shiyang.mapper.TbUserMapper;
import com.shiyang.pojo.*;
import com.shiyang.service.FriendService;
import com.shiyang.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author ShiYang
 * @title FriendServiceImpl
 * @description
 * @date 2019/07/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserService userService;
    @Autowired
    private TbFriendReqMapper friendReqMapper;
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private TbFriendMapper friendMapper;

    @Override
    public void sendRequest(TbFriendReq tbFriendReq) {
        userService.checkAllowToAddFriend(tbFriendReq.getFromUserid(), tbFriendReq.getToUserid());
        TbFriendReq friendReq = new TbFriendReq();
        friendReq.setId(idWorker.nextId());
        friendReq.setFromUserid(tbFriendReq.getFromUserid());
        friendReq.setToUserid(tbFriendReq.getToUserid());
        friendReq.setStatus(0);
        friendReq.setCreatetime(new Date());
        friendReqMapper.insert(friendReq);
    }

    @Override
    public List<User> findFriendReq(String userid) {
        TbFriendReqExample friendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria = friendReqExample.createCriteria();
        criteria.andToUseridEqualTo(userid);
        criteria.andStatusEqualTo(0);
        List<TbFriendReq> friendReqs = friendReqMapper.selectByExample(friendReqExample);
        ArrayList<User> friendList = new ArrayList<>();
        // 加载好友信息
        for (TbFriendReq friendReq : friendReqs) {
            TbUser tbUser = userMapper.selectByPrimaryKey(friendReq.getFromUserid());
            User friend = new User();
            BeanUtils.copyProperties(tbUser, friend);
            friend.setId(friendReq.getId());
            friendList.add(friend);
        }
        return friendList;
    }

    @Override
    public void acceptFriendReq(String reqid) {
        // 修改好友请求状态为1
        TbFriendReq tbFriendReq = friendReqMapper.selectByPrimaryKey(reqid);
        tbFriendReq.setStatus(1);
        friendReqMapper.updateByPrimaryKeySelective(tbFriendReq);

        // 分别添加好友关系表
        TbFriend friend1 = new TbFriend();
        friend1.setId(idWorker.nextId());
        friend1.setCreatetime(new Date());
        friend1.setFriendsId(tbFriendReq.getToUserid());
        friend1.setUserid(tbFriendReq.getFromUserid());

        TbFriend friend2 = new TbFriend();
        friend2.setId(idWorker.nextId());
        friend2.setCreatetime(new Date());
        friend2.setFriendsId(tbFriendReq.getFromUserid());
        friend2.setUserid(tbFriendReq.getToUserid());
        friendMapper.insert(friend1);
        friendMapper.insert(friend2);

        // 发消息,在app主页面更新通讯录
        // 获取发送好友请求方的Channel
//        Map<Object, Object> UserChannelMap = new ConcurrentHashMap<>(16);
//        Channel channel =(Channel) UserChannelMap.get(tbFriendReq.getFromUserid());
//        if (channel != null) {
//        }
    }

    @Override
    public void ignoreFriendReq(String reqid) {
        // 修改好友请求状态为1
        TbFriendReq tbFriendReq = friendReqMapper.selectByPrimaryKey(reqid);
        tbFriendReq.setStatus(1);
        friendReqMapper.updateByPrimaryKeySelective(tbFriendReq);
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria criteria = friendExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        List<TbFriend> friendList = friendMapper.selectByExample(friendExample);
        ArrayList<User> users = new ArrayList<>();
        for (TbFriend friend : friendList) {
            User user = new User();
            TbUser tbUser = userMapper.selectByPrimaryKey(friend.getFriendsId());
            BeanUtils.copyProperties(tbUser,user);
            users.add(user);
        }
        return users;
    }
}
