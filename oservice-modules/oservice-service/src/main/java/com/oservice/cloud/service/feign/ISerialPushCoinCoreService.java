package com.oservice.cloud.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 推币机串口核心服务
 */
@FeignClient(value = "os-mina-pushcoin",path ="directive")
public interface ISerialPushCoinCoreService {
    @GetMapping(value = "/{id}/{data}/{remark}")
    public void deliverDataReceive(@PathVariable String id, @PathVariable byte[] data, @PathVariable String remark);
}
