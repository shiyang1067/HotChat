package com.shiyang.service.impl;

import com.shiyang.mapper.TbChatRecordMapper;
import com.shiyang.pojo.TbChatRecord;
import com.shiyang.pojo.TbChatRecordExample;
import com.shiyang.service.ChatRecordService;
import com.shiyang.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * ChatRecordServiceImpl
 *
 * @author ShiYang
 * @date 2019/08/02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatRecordServiceImpl implements ChatRecordService {
    @Autowired
    private TbChatRecordMapper chatRecordMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasDelete(0);
        chatRecord.setHasRead(0);
        chatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample chatRecordExample = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria1 = chatRecordExample.createCriteria();
        TbChatRecordExample.Criteria criteria2 = chatRecordExample.createCriteria();
        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);

        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);

        chatRecordExample.or(criteria1);
        chatRecordExample.or(criteria2);

        // 更新好友发给用户的消息为已读
        TbChatRecordExample querySentToMeExample = new TbChatRecordExample();
        TbChatRecordExample.Criteria querySentToMeCriteria = querySentToMeExample.createCriteria();
        querySentToMeCriteria.andFriendidEqualTo(userid);
        querySentToMeCriteria.andHasReadEqualTo(0);
        List<TbChatRecord> chatRecordList = chatRecordMapper.selectByExample(querySentToMeExample);
        for (TbChatRecord chatRecord : chatRecordList) {
            chatRecord.setHasRead(1);
            chatRecordMapper.updateByPrimaryKeySelective(chatRecord);
        }
        return chatRecordMapper.selectByExample(chatRecordExample);
    }

    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample chatRecordExample = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = chatRecordExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        return chatRecordMapper.selectByExample(chatRecordExample);
    }

    @Override
    public void updateStatusHasRead(String id) {
        TbChatRecord chatRecord = new TbChatRecord();
        chatRecord.setId(id);
        chatRecord.setHasRead(1);
        chatRecordMapper.updateByPrimaryKeySelective(chatRecord);
    }
}
