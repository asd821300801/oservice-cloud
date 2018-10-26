package com.oservice.mina.serial.session.impl;

import com.alibaba.fastjson.JSON;
import com.oservice.mina.serial.exception.PrintException;
import com.oservice.mina.serial.net.IoHandler;
import com.oservice.mina.serial.session.SessionManager;
import com.oservice.mina.util.ByteArrayUtil;
import com.oservice.mina.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionManagerImpl implements SessionManager {

    private static Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

    private static ConcurrentHashMap<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
    private static Vector<IoSession> preSessions = new Vector<>();

    @Override
    public void addPreSession(IoSession session, List<String> hexs) {
        session.setAttribute(IoHandler.LASTEST_TIME, new Date());
        if (!preSessions.contains(session)) {
            preSessions.add(session);
        }

        logger.info("sessionIP:" + session.getRemoteAddress().toString() + ",hexs:" + JSON.toJSON(hexs));

        //默认连接下位机指令
        byte[] data = ByteArrayUtil.getCon();

        logger.info("addPreSession:" + JSON.toJSON(ByteArrayUtil.bytesToHexs(data)));
        push(session, data);
    }

    @Override
    public void updateSession(IoSession session) {
        if (session != null && session.getRemoteAddress() != null) {
            session.setAttribute(IoHandler.LASTEST_TIME, new Date());
            String id = (String) session.getAttribute(IoHandler.ID);
            if (StringUtils.isNotBlank(id))
                sessionMap.put(id, session);
            preSessions.remove(session);
        }
    }

    @Override
    public void putSession(IoSession session) {
        if (session != null && session.getRemoteAddress() != null) {
            session.setAttribute(IoHandler.LASTEST_TIME, new Date());
            String id = (String) session.getAttribute(IoHandler.ID);
            logger.info("putSession id:" + id);
            if (StringUtils.isNotBlank(id))
                sessionMap.put(id, session);
        }
    }

    @Override
    public void delSession(IoSession session) {
        Object idObj = session.getAttribute(IoHandler.ID);
        if (idObj != null) {
            String id = (String) idObj;
            sessionMap.remove(id);
        }
        try {
            session.closeNow();
        } catch (Exception e) {
            logger.error("delSession Error " + PrintException.getErrorStack(e, 0));
        }
    }

    @Override
    public void push(String id, byte[] data) {
        IoSession session = sessionMap.get(id);
        if (session != null && session.getRemoteAddress() != null)
            deliver(session, data);
    }

    @Override
    public void push(IoSession session, byte[] data) {
        deliver(session, data);
    }

    private void deliver(IoSession ioSession, byte[] data) {
        boolean errorDelivering = false;
        try {
            ioSession.write(IoBuffer.wrap(data));//关键，传递数组的关键
        } catch (Exception e) {
            logger.error("Exception Error delivering data" + PrintException.getErrorStack(e, 0));
            errorDelivering = true;
        }
        if (errorDelivering) {
            logger.error("delSession delivering packet:");
            delSession(ioSession);
        }
    }

    @Override
    public void managePreSession() {
        try {
            List<IoSession> preSessionsClone = new ArrayList<>(preSessions);
            Iterator<IoSession> iterator = preSessionsClone.iterator();
            while (iterator.hasNext()) {
                IoSession session = iterator.next();
                try {
                    if (session.getRemoteAddress() == null) {
                        logger.info("managePreSession del r " + session.getAttribute(IoHandler.LASTEST_TIME));
                        preSessions.remove(session);
                        session.closeNow();
                    } else {
                        Date lastestDate = (Date) session.getAttribute(IoHandler.LASTEST_TIME);
                        int minute = DateUtils.minuteBetween(lastestDate);
                        if (minute > 3) {
                            byte[] data = ByteArrayUtil.getFactoryReset();
                            push(session, data);
                            logger.info("managePreSession update&del t " + session.getRemoteAddress());
                            preSessions.remove(session);
                            session.closeOnFlush();
                        }
                    }
                } catch (Exception e) {
                    logger.info("managePreSession del e " + session.getAttribute(IoHandler.LASTEST_TIME));
                    preSessions.remove(session);
                    session.closeNow();
                }
            }
        } catch (Exception e) {
            logger.info("managePreSession error:" + PrintException.getErrorStack(e, 0));
        }
    }

    @Override
    public void manageSession() {
        try {
            //普通session
            Iterator<Entry<String, IoSession>> iterator = sessionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, IoSession> entry = iterator.next();
                IoSession session = entry.getValue();
                try {
                    if (session.getRemoteAddress() == null) {
                        logger.info("manageSession del r " + session.getAttribute(IoHandler.LASTEST_TIME));
                        iterator.remove();
                        session.closeNow();
                    } else {
                        Date lastestDate = (Date) session.getAttribute(IoHandler.LASTEST_TIME);
                        int minute = DateUtils.minuteBetween(lastestDate);
                        if (minute > 10) {
                            logger.info("manageSession del t " + session.getAttribute(IoHandler.LASTEST_TIME));
                            iterator.remove();
                            session.closeOnFlush();
                        }
                    }
                } catch (Exception e) {
                    logger.info("manageSession del e " + session.getAttribute(IoHandler.LASTEST_TIME));
                    iterator.remove();
                    session.closeNow();
                }
            }
        } catch (Exception e) {
            logger.info("manageSession error:" + PrintException.getErrorStack(e, 0));
        }
    }
}
