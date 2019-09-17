package com.hikvision.aimms.netty;

import com.hikvision.aimms.common.Pair;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 *
 */
public class NettyRemotingAbstract {

    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    /**
     * 处理请求后的body
     */
    public void processRequestBody(final ChannelHandlerContext ctx, final String body) {


        if (defaultRequestProcessor.getObject1().rejectRequest()) {
            return;
        }

        Runnable run = new Runnable() {
            @Override
            public void run() {

            }
        };

//        this.defaultRequestProcessor.getObject2().submit(run);
    }
}
