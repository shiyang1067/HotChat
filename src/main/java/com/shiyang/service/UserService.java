package com.shiyang.service;

import com.shiyang.pojo.TbUser;
import com.shiyang.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ShiYang
 * @title UserService
 * @description 用户服务接口
 * @date 2019/07/16
 */
public interface UserService {

    /**
     * 返回数据库中的所有用户
     *
     * @return List<TbUser>
     */
    List<TbUser> findAll();

    /**
     * 判断用户是否登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    User login(String username, String password);

    /**
     * 注册用户
     *
     * @param tbUser 用户信息
     */
    void register(TbUser tbUser);

    /**
     * 上传头像
     *
     * @param file   上传的文件
     * @param userid 用户id
     * @return User 如果上传成功,返回用户信息,否则,返回null
     */
    User upload(MultipartFile file, String userid);

    /**
     * 更新用户昵称
     *
     * @param id       用户id
     * @param nickname 昵称
     */
    void updateNickname(String id, String nickname);

    /**
     * 根据用户id查询用户信息
     *
     * @param userid 用户id
     * @return com.shiyang.pojo.User
     */
    User findById(String userid);

    /**
     * 根据用户名搜索用户
     *
     * @param userid         用户id
     * @param friendUsername 用户名
     * @return com.shiyang.pojo.User
     */
    User findByUsername(String userid, String friendUsername);

    /**
     * 检查是否允许添加好友
     *
     * @param userid 用户id
     * @param friendId 要添加的好友id
     */
    void checkAllowToAddFriend(String userid, String friendId);
}
