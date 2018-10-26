package com.oservice.mina.serial.net;

import com.alibaba.fastjson.JSON;
import com.oservice.mina.serial.exception.PrintException;
import com.oservice.mina.serial.handler.DataReceiveHandler;
import com.oservice.mina.serial.session.SessionManager;
import com.oservice.mina.util.ByteArrayUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;


public class IoHandler extends IoHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(IoHandler.class);

    public static final String LASTEST_TIME = "LASTEST_TIME";
    public static final String STATE = "STATE";
    public static final String ID = "ID";

    @Resource
    private SessionManager sessionManager;

    @Resource
    private DataReceiveHandler dataReceiveHandler;


    /**
     * 建立Session
     */
    public void sessionOpened(IoSession session) throws Exception {
        sessionManager.addPreSession(session, null);
    }

    /**
     * 消息处理
     */
    @Override
    public void messageReceived(IoSession iosession, Object message) {
        try {
            byte[] bytes = (byte[]) message;
            List<String> hexs = ByteArrayUtil.bytesToHexs(bytes);
            String id = "";
            try {
                id = iosession.getAttribute(IoHandler.ID).toString();
            } catch (Exception e) {

            }
            logger.info("messageReceived id:" + id + " message:" + JSON.toJSONString(hexs));

            //校验包头
            if (!hexs.get(0).equals("8a")) return;

            //认证
            if (iosession.getAttribute(IoHandler.ID) == null) {
                logger.info("进入认证");
                if ((hexs.get(2).equals("02"))) {//完成认证
                    dataReceiveHandler.process(iosession, hexs);
                } else {//进入认证
                    sessionManager.addPreSession(iosession, hexs);
                }
            } else {//数据处理
                dataReceiveHandler.process(iosession, hexs);
            }
        } catch (Exception e) {
            logger.error("Unknown messageReceived " + PrintException.getErrorStack(e, 0));
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        sessionManager.delSession(session);
    }

    @Override
    public void sessionClosed(IoSession iosession) throws Exception {
        logger.error("sessionClosed.wait");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error("Mina exception:" + cause);
        logger.error("Mina exception:" + PrintException.getErrorStack(cause, 0));
    }

}
