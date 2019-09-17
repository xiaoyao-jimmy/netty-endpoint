package com.hikvision.aimms.netty;

import com.hikvision.aimms.codec.AimmsAlarmDecoder;
import com.hikvision.aimms.common.Pair;
import com.hikvision.aimms.remoting.RemotingServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * aimms sdk 客户端 用来监听 open api 发送的 报警事件
 */
public class NettyRemotingServer extends NettyRemotingAbstract implements RemotingServer {


    private ServerBootstrap serverBootstrap;
    private NettyServerConfig nettyServerConfig;

    public NettyRemotingServer(NettyServerConfig nettyServerConfig) {
        this.serverBootstrap = new ServerBootstrap();
        this.nettyServerConfig = nettyServerConfig;
    }

    private boolean useEpoll() {
        return nettyServerConfig.isUseEpollNativeSelector();
    }

    @Override
    public void start() {
        ServerBootstrap serverBootstrap =
            this.serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class).localAddress(nettyServerConfig.getListenPort())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())
                    .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketRcvBufSize())
                    .localAddress(this.nettyServerConfig.getListenPort())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(1048576))
                                    .addLast(new AimmsAlarmDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });

        try {
            ChannelFuture sync = serverBootstrap.bind().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void registerDefaultProcessor(NettyRequestProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<>(processor, executor);
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String body) throws Exception {
            processRequestBody(ctx, body);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            String responseBody = "";
            System.out.println("over");
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseBody, CharsetUtil.UTF_8));
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }


    /**
     * example to use
     * @param args
     */
    public static void main(String[] args) {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        NettyRemotingServer nettyRemotingServer = new NettyRemotingServer(nettyServerConfig);
        NettyRequestProcessor processor = new NettyRequestProcessor() {
            @Override
            public String processRequest(ChannelHandlerContext ctx, String body) {
                System.out.println("在这里添加业务处理");
                return null;
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        };
        nettyRemotingServer.registerDefaultProcessor(processor, Executors.newFixedThreadPool(10));
        nettyRemotingServer.start();
    }
}
