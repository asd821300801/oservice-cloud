package com.oservice.mina.serial.handler;


import com.alibaba.fastjson.JSON;
import com.oservice.mina.serial.session.SessionManager;
import com.oservice.mina.util.ByteArrayUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 发送消息处理
 * @author LingDu
 */
@Service
public class DataSendHandler extends AbstractHandler{

    @Resource
    private SessionManager sessionManager;

    public void handleDirective(String id, byte[] data) {
        logger.info("handleDirective id:" + id + " data:" + JSON.toJSONString(ByteArrayUtil.bytesToHexs(data)));
        sessionManager.push(id, data);
    }
}
