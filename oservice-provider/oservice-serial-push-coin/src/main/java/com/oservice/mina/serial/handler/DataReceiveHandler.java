package com.oservice.mina.serial.handler;

import com.alibaba.fastjson.JSON;

import com.oservice.mina.feign.IAppCoreRemoteService;
import com.oservice.mina.serial.net.IoHandler;
import com.oservice.mina.serial.session.SessionManager;
import com.oservice.mina.util.ByteArrayUtil;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 接收消息处理
 *
 * @author Max
 */
@Service
public class DataReceiveHandler extends AbstractHandler {

    @Resource
    private SessionManager sessionManager;

    @Resource
    private IAppCoreRemoteService iAppCoreRemoteService;

    /**
     * 消息处理
     *
     * @param session
     * @param hexs
     */
    public void process(IoSession session, List<String> hexs) {
        boolean callback = true;
        switch (hexs.get(2)) {
            case "02"://连接到下位机
                List<String> idList = hexs.subList(4, 11);
                String deviceId = ByteArrayUtil.getArrayStr(idList);
                session.setAttribute(IoHandler.ID, deviceId);
                sessionManager.updateSession(session);
                logger.info("连接成功:" + session.getAttribute(IoHandler.ID) + " code:" + JSON.toJSONString(hexs));

                //insert into machine_device (deviceId,createTime,time,lastBeatTime) value (?,now(),now(),now()) on duplicate key update time=now(),lastBeatTime=now()
                //update machine_device set status=0 where status=2 and deviceId=? limit 1
                callback = false;
                break;
            case "10"://心跳
                if (hexs.get(3).equals("03")) {//心跳
                    if (hexs.get(4).equals("00") && hexs.get(5).equals("00")) {
                        //update machine_device set lastBeatTime=now()  where deviceId=?
                        //update machine_device set status=0 where status=2 and deviceId=? limit 1
                        sessionManager.updateSession(session);
                        callback = false;
                    }
                }
                break;
            default:

                break;
        }
        //回调
        if (callback) {
            iAppCoreRemoteService.deliverDataReceive(session.getAttribute(IoHandler.ID).toString(), hexs);
        }
    }

}
