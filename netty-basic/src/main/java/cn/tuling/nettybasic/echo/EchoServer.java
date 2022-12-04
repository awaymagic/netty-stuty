package cn.tuling.nettybasic.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 作者：Mark
 * 类说明：基于Netty的服务器
 */

public class EchoServer  {
    private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        LOG.info("服务器即将启动");
        echoServer.start();
        LOG.info("服务器关闭");
    }

    public void start() throws InterruptedException {
        /*1.线程组*/
        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            /*2. ServerBootstrap 服务端启动必备 */
            ServerBootstrap b = new ServerBootstrap();
            // ServerBootstrap 绑定 EventLoopGroup
            b.group(group)
                    // 指定什么协议通讯,比如 BIO - NioServerSocketChannel 监听连接
                    .channel(NioServerSocketChannel.class)
                    // 指定哪个端口消费
                    .localAddress(new InetSocketAddress(port))
                    // 要经过那些 Handler, childHandler 指定子Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            /*异步绑定bind()到服务器，sync()会阻塞到完成*/
            ChannelFuture f = b.bind().sync();
            LOG.info("--- 服务器启动完成 ---");
            /*(关闭动作)阻塞当前线程，直到服务器的ServerChannel被关闭*/
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }


}
