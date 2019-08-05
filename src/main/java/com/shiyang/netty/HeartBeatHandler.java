package com.shiyang.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author ShiYang
 * @title HeartBeatHandler
 * @description
 * @date 2019/08/02
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent =(IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE){
                System.out.println("读空闲");
            }
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                System.out.println("写空闲");
            }
            if (idleStateEvent.state() == IdleState.ALL_IDLE){
                System.out.println("读写空闲");
                System.out.println("关闭通道资源");
                ctx.channel().close();
            }
        }
    }
}
