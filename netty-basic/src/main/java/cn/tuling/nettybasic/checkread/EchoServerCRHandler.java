package cn.tuling.nettybasic.checkread;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*观察channelRead和channelReadComplete*/
public class EchoServerCRHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EchoServerCRHandler.class);
    private int readCount = 0;
    private int readCompleteCount = 0;

    /**
     * channelRead 和 channelReadComplete 区别
     * 比如 buf 长200，每次消息40，则 channelRead 执行5次，而 channelReadComplete 执行1次
     * 报文很大或产生分包时,一次发送未发完会出现 channelReadComplete 次数 > channelRead
     *
     * 总结: channelReadComplete 读物 buf 则执行一次，channelRead 在处理完半包后 和应用消息数量一致
     * 推荐使用 channelRead
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf in = (ByteBuf)msg;
        LOG.info("channelRead执行了" + (++readCount) + "次");
        //ctx.writeAndFlush(in);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channelReadComplete执行了" + (++readCompleteCount) + "次");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
