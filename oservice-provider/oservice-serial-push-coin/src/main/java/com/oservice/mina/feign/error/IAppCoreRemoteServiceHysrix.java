package com.oservice.mina.feign.error;

import com.oservice.mina.feign.IAppCoreRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class IAppCoreRemoteServiceHysrix implements IAppCoreRemoteService {

    @Override
    public void deliverDataReceive(String deviceId, List<String> hexs) {
        log.info("feign deliver Data Receive hysrix ..... ");
    }
}
