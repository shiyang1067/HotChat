package com.shiyang.service;

import com.shiyang.pojo.TbChatRecord;

import java.util.List;

/**
 * @author ShiYang
 * @title ChatRecordService
 * @description
 * @date 2019/08/02
 */
public interface ChatRecordService {
    /**
     * 新增聊天记录
     *
     * @param chatRecord 1
     */
    void insert(TbChatRecord chatRecord);

    /**
     * 根据用户id和好友id查询聊天记录
     *
     * @param userid 用户id
     * @param friendid 好友id
     * @return java.util.List<com.shiyang.pojo.TbChatRecord>
     */
    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);

    /**
     * 根据用户id查询未读消息
     *
     * @param userid 1
     * @return java.util.List<com.shiyang.pojo.TbChatRecord>
     */
    List<TbChatRecord> findUnreadByUserid(String userid);

    /**
     * 更改消息未读状态为已读
     *
     * @param id
     */
    void updateStatusHasRead(String id);
}
