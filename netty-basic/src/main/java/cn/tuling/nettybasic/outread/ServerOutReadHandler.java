package cn.tuling.nettybasic.outread;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author ：Mark老师
 * @description ：TODO
 */
public class ServerOutReadHandler extends ChannelOutboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ServerOutReadHandler.class);
    private int count;

    /**
     * read()
     * 业务方要求读更多的数据的动作，会被 netty 打包成事件在 pipeline 上进行传播
     * 而这个动作则会触发 ChannelOutboundHandlerAdapter 的 read()
     * 读的要求(事件包括数据和动作)是出站  (MASK_READ)
     */
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        LOG.info("请求读更多的数据，但是我要休息一下");
        TimeUnit.SECONDS.sleep(5);
        super.read(ctx);
//        LOG.info("想读数据，我不同意，我不把请求往前传递");

    }

    /**
     * 如果派生至 ChannelInboundHandlerAdapter ,用的是 channelRead()
     * 而自己加了 handler 而未手动处理 handler 接下来的操作，会导致在 write() 这里终止操作
     * 所以一般派生至 SimpleChannelInboundHandler ,用的是 channelRead0() 在上下都做了释放,
     * 内部覆盖了 channelRead()，暴露了 channelRead0()
     *
     * 总结:写入站 handler 时, channelRead() 读到的数据要么向下执行，要么释放掉。避免内存泄漏
     * 嫌麻烦就入站直接使用 SimpleChannelInboundHandler ,开发者只专注于业务即可
     */
    // @Override
    // public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    //     super.write(ctx, msg, promise);
    // }

}
