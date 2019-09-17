package com.hikvision.aimms.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.util.StringUtils;

/**
 * aimms 告警信息 解码处理 http 支持 告警队列
 */
public class AimmsAlarmDecoder extends ChannelInboundHandlerAdapter {


    private static final String AIMMS_ALARM_URL = "/aimms/v1/alarm/info";

    /**
     * 处理 http协议
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String path = request.uri();
            String body = request.content().toString(CharsetUtil.UTF_8);

            if (!StringUtils.isEmpty(path) && path.contains(AIMMS_ALARM_URL)) {
                super.channelRead(ctx, body);
            }
        }
    }
}
