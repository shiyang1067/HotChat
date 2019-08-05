package com.shiyang.controller;

import com.shiyang.pojo.Result;
import com.shiyang.pojo.TbChatRecord;
import com.shiyang.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatRecordController
 *
 * @author ShiYang
 * @date 2019/08/02
 */
@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {
    @Autowired
    private ChatRecordService chatRecordService;

    @RequestMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        try {
            List<TbChatRecord> chatRecordList = chatRecordService.findByUserIdAndFriendId(userid, friendid);
            return chatRecordList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }

    @RequestMapping("/findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        try {
            List<TbChatRecord> chatRecordList = chatRecordService.findUnreadByUserid(userid);
            return chatRecordList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }
}
