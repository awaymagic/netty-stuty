package cn.tuling.nettyadv.server;

import cn.tuling.nettyadv.kryocodec.KryoDecoder;
import cn.tuling.nettyadv.kryocodec.KryoEncoder;
import cn.tuling.nettyadv.server.asyncpro.DefaultTaskProcessor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @author Mark老师
 * 类说明：
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 监控 handler
        // ch.pipeline().addLast(new MetricsHandler());
        /*粘包半包问题*/
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,
                0,2,0,
                2));
        // 加 2 个字节
        ch.pipeline().addLast(new LengthFieldPrepender(2));

        /*序列化相关*/
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());

        /*处理心跳超时*/
        // 链路的读空闲检测, 服务器在 15 秒后要收到客户端消息
        ch.pipeline().addLast(new ReadTimeoutHandler(15));

        // 认证
        ch.pipeline().addLast(new LoginAuthRespHandler());
        // 心跳应答
        ch.pipeline().addLast(new HeartBeatRespHandler());
        // 业务处理
        ch.pipeline().addLast(new ServerBusiHandler(new DefaultTaskProcessor()));

    }
}
