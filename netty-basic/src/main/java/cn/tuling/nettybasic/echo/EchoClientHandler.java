package cn.tuling.nettybasic.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 作者：Mark
 * 类说明：客户端的业务Handler
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /*
     * 为什么会用 SimpleChannelInboundHandler ?
     * netty 帮我们实现的
     * channelRead0() 读网络上的某数据
     */

    /**读取到网络数据后进行业务处理,并关闭连接*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("client Accept" + msg.toString(CharsetUtil.UTF_8));
        // 关闭连接
        // ctx.close();
    }

    /**
     * channel活跃后，做业务处理
     * 客户端和服务器连接成功后，客户端会激活 channelActive() ,执行方法里的内容(往服务器发内容)
     * 服务器的 channelRead() 读到后,又把内容发送给浏览器 channelRead0()
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer(
                "Hello,Netty",CharsetUtil.UTF_8));
       // ctx.pipeline().write()
       // ctx.channel().write()
        ctx.alloc().buffer();
    }
}
