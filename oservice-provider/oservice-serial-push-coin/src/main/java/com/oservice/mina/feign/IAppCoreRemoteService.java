package com.oservice.mina.feign;


import com.oservice.mina.feign.error.IAppCoreRemoteServiceHysrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 核心模块远程调用接口
 */
@FeignClient(value = "os-service",path = "serial",fallback = IAppCoreRemoteServiceHysrix.class)
public interface IAppCoreRemoteService {
    @RequestMapping(value = "/{deviceId}/{hexs}",method = RequestMethod.GET)
    public void deliverDataReceive(@RequestParam("deviceId") String deviceId, @RequestParam("hexs") List<String> hexs);
}
