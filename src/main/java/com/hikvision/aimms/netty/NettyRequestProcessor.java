package com.hikvision.aimms.netty;

import io.netty.channel.ChannelHandlerContext;

public interface NettyRequestProcessor {
    /**
     * 告警http处理后  报文透传
     * @param ctx
     * @param body
     * @return
     */
    String processRequest(ChannelHandlerContext ctx, String body);

    boolean rejectRequest();
}
