package com.oservice.mina.schedule;


import com.oservice.mina.serial.session.SessionManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * session定时任务
 * @author LingDu
 */
@Component
public class sessionManagerTask {

	@Resource
	private SessionManager sessionManager;

	@PostConstruct
	public void doCron() {
		new Thread(() -> {
			while (true) {
                try {
					// 机器故障  machineStateService.handleMachineBrokentask();
					// update machine_device set status=2 where lastBeatTime<DATE_SUB(now(),INTERVAL ? MINUTE)
					sessionManager.managePreSession();
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
	}

}
