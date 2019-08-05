package com.shiyang.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ShiYang
 * @title UserChannelMap
 * @description
 * @date 2019/08/02
 */
public class UserChannelMap {
    /**
     * 用于保存用户id与通道的Map对象
     */
    private static Map<String, Channel> userChannelMap;

    static {
        userChannelMap = new HashMap<String, Channel>(16);
    }

    /**
     * 添加用户id与channel的关联
     *
     * @param userid  用户id
     * @param channel 通道
     */
    public static void put(String userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    /**
     * 移除用户id与channel的关联
     *
     * @param userid  用户id
     * @param channel 通道
     */
    public static void remove(String userid, Channel channel) {
        userChannelMap.remove(userid, channel);
    }

    /**
     * 根据通道的id移除用户与Channel的关联
     *
     * @param channelId 通道id
     */
    public static void removeByChannelId(String channelId) {
        if (!StringUtils.isNotBlank(channelId)) {
            return;
        }
        for (String s : userChannelMap.keySet()) {
            Channel channel = userChannelMap.get(s);
            if (channelId.equals(channel.id().asLongText())) {
                userChannelMap.remove(s);
                System.out.println("客户端连接断开,取消用户:" + s + " 与通道:" +
                        channel.id().asLongText() + "的关联");
                break;
            }
        }
    }

    /**
     * 打印通道的信息
     */
    public static void print() {
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户id" + s + "通道: " + userChannelMap.get(s).id());
        }
    }

    public static Channel get(String friendid) {
        return userChannelMap.get(friendid);
    }
}
