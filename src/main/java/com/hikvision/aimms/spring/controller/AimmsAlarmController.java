package com.hikvision.aimms.spring.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 告警接受 可以直接结合 spring 使用
 * 如果不使用 spring 请使用 netty 入口
 */
@RestController
@ConditionalOnExpression
public class AimmsAlarmController {

    /**
     * 接受
     * @return
     */
    @PostMapping("/aimms/v1/alarm/info")
    public PrettyReponse alarmInfo() {
        // 业务逻辑
        return null;
    }



    class PrettyReponse {}
}
