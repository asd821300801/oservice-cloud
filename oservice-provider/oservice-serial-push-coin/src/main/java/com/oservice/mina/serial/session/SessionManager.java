package com.oservice.mina.serial.session;

import org.apache.mina.core.session.IoSession;

import java.util.List;

public interface SessionManager {

    //session处理

    /**
     * 添加preSession
     *
     * @param session
     */
    public void addPreSession(IoSession session, List<String> hexs);

    /**
     * 更新session
     *
     * @param session
     */
    public void updateSession(IoSession session);

    /**
     * 删除session
     *
     * @param session
     * @return
     */
    public void delSession(IoSession session);

    //消息处理

    /**
     * 推送消息
     *
     * @param id
     * @param data
     */
    public void push(String id, byte[] data);

    /**
     * 推送消息
     *
     * @param session
     * @param data
     */
    public void push(IoSession session, byte[] data);

    //session管理

    /**
     * 管理preSession
     * 超过3分钟没有心跳(session未更新) 则断开连接
     */
    public void managePreSession();

    /**
     * 管理session
     */
    public void manageSession();

    void putSession(IoSession session);
}
