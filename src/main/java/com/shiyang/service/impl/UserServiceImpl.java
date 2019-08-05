package com.shiyang.service.impl;

import com.shiyang.utils.FastDFSClient;
import com.shiyang.utils.IdWorker;
import com.shiyang.utils.QRCodeUtils;
import com.shiyang.mapper.TbFriendMapper;
import com.shiyang.mapper.TbFriendReqMapper;
import com.shiyang.mapper.TbUserMapper;
import com.shiyang.pojo.*;
import com.shiyang.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @title UserServiceImpl
 * @description 用户服务实现
 * @author ShiYang
 * @date 2019/07/16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private Environment env;
    @Autowired
    private TbFriendMapper friendMapper;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private TbFriendReqMapper friendReqMapper;

    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();
            criteria.andUsernameEqualTo(username);
            List<TbUser> tbUsers = userMapper.selectByExample(example);
            if (tbUsers != null && tbUsers.size() == 1) {
                // 对密码进行校验
                String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
                if (encodingPassword.equals(tbUsers.get(0).getPassword())) {
                    User user = new User();
                    BeanUtils.copyProperties(tbUsers.get(0), user);
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void register(TbUser tbUser) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(tbUser.getUsername());
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers != null && tbUsers.size() > 0) {
            throw new RuntimeException("用户名已存在!");
        } else {
            tbUser.setId(idWorker.nextId());
            // md5加密
            tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
            tbUser.setNickname(tbUser.getUsername());

            String tmpFolder = env.getProperty("hcat.tmpdir");
            String qrCodeFile = tmpFolder + "/" + tbUser.getUsername() + ".png";
            qrCodeUtils.createQRCode(qrCodeFile, "user_code:" + tbUser.getUsername());
            try {
                String url = fastDFSClient.uploadFile(new File(qrCodeFile));
                String prefix = env.getProperty("fdfs.httpurl");
                tbUser.setQrcode(prefix + url);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("上传文件失败!");
            }
            tbUser.setCreatetime(new Date());
            userMapper.insert(tbUser);
        }
    }

    @Override
    public User upload(MultipartFile file, String userid) {
        try {
            // 返回FastDFS中的url,相对路径,不带http://192.168.0.1
            String url = fastDFSClient.uploadFace(file);
            // 在FastDFS上传时,会自动生成一个缩略图
            // 文件名_150x150.后缀
            String[] fileNameList = url.split("\\.");
            String fileName = fileNameList[0];
            String ext = fileNameList[1];

            String picSmallUrl = fileName + "_150x150." + ext;

            String prefix = env.getProperty("fdfs.httpurl");
            TbUser tbUser = userMapper.selectByPrimaryKey(userid);

            // 设置大图片
            tbUser.setPicNormal(prefix + url);
            // 设置缩略图
            tbUser.setPicSmall(prefix + picSmallUrl);
            // 将新的头像url更新到数据库中
            userMapper.updateByPrimaryKey(tbUser);

            //将用户信息返回
            User user = new User();
            BeanUtils.copyProperties(tbUser, user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateNickname(String id, String nickname) {
        if (StringUtils.isNotBlank(nickname)) {
            TbUser tbUser = new TbUser();
            tbUser.setId(id);
            tbUser.setNickname(nickname);
            userMapper.updateByPrimaryKeySelective(tbUser);
        } else {
            throw new RuntimeException("昵称不能为空");
        }
    }

    @Override
    public User findById(String userid) {
        TbUser tbUser = userMapper.selectByPrimaryKey(userid);
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);
        return user;
    }

    @Override
    public User findByUsername(String userid, String friendUsername) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(friendUsername);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers == null || tbUsers.size() == 0) {
            return null;
        }
        TbUser friend = tbUsers.get(0);
        User friendUser = new User();
        BeanUtils.copyProperties(friend, friendUser);
        return friendUser;
    }


    @Override
    public void checkAllowToAddFriend(String userid, String friendId) {
        // 1.用户不能添加自己
        if (userid.equals(friendId)) {
            throw new RuntimeException("不能添加自己为好友");
        }

        // 2.用户不能重复添加
        // 如果用户已经添加该好友,就不能再次添加
        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria friendCriteria = friendExample.createCriteria();
        friendCriteria.andUseridEqualTo(userid);
        friendCriteria.andFriendsIdEqualTo(friendId);
        List<TbFriend> friendList = friendMapper.selectByExample(friendExample);
        if (friendList != null && friendList.size() > 0) {
            throw new RuntimeException( "已经是您的好友了!");
        }

        // 3.判断是否已经提交好友申请,不能重复提交,如果已经提交直接抛异常
        TbFriendReqExample friendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria friendReqCriteria = friendReqExample.createCriteria();
        friendReqCriteria.andToUseridEqualTo(friendId);
        friendReqCriteria.andFromUseridEqualTo(userid);
        // 好友请求没有被处理
        friendReqCriteria.andStatusEqualTo(0);
        List<TbFriendReq> friendReqList = friendReqMapper.selectByExample(friendReqExample);
        if (friendReqList != null && friendReqList.size() > 0) {
            throw new RuntimeException("已经提交好友请求!");
        }
    }

}
