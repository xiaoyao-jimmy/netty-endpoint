package com.hikvision.aimms.remoting;

import com.hikvision.aimms.netty.NettyRequestProcessor;

import java.util.concurrent.ExecutorService;

public interface RemotingServer extends RemotingService {

    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);
}
