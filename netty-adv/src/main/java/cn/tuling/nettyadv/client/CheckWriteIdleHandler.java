package cn.tuling.nettyadv.client;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author ：Mark老师
 * @description ：客户端检测自己的写空闲
 */
public class CheckWriteIdleHandler extends IdleStateHandler {
    /**
     * 写空闲检测时间 15/2 为 8s
     * 客户端在 8 秒未向服务器发送报文时，则要发送一个心跳报文
     * 产生一个空闲事件
     */
    public CheckWriteIdleHandler() {
        super(0, 8, 0);
    }
}
