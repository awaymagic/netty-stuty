package cn.tuling.nettybasic.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 作者：Mark
 * 类说明：基于Netty的客户端
 */
public class EchoClient {

    private final int port;
    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {

        /*线程组*/
        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            /* Bootstrap 客户端启动必备，和服务器的不同点1*/
            Bootstrap b = new Bootstrap();
            b.group(group)
                    /*指定使用NIO的通信模式*/
                    .channel(NioSocketChannel.class)
                    /*连结:指定服务器的IP地址和端口，和服务器的不同点2*/
                    .remoteAddress(new InetSocketAddress(host,port))
                    /* handler():和服务器的不同点3*/
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());

                        }
                    });
            /*异步连接connect()到服务器，sync()会阻塞到完成，和服务器的不同点*/
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();/*阻塞当前线程，直到客户端的Channel被关闭*/
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999,"127.0.0.1").start();
    }
}
