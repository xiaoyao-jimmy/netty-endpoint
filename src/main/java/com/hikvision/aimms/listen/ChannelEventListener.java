package com.hikvision.aimms.listen;

import io.netty.channel.Channel;

/**
 * 业务方实现自己的listener处理报文
 *
 * @author shixiao
 */
public interface ChannelEventListener {

    /**
     * 业务方 实现 此接口 并且初始化内容
     * @param remoteAddr
     * @param channel
     */
    void onChannelComplete(final String remoteAddr, final Channel channel);
}
