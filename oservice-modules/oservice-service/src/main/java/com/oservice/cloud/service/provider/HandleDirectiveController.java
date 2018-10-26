package com.oservice.cloud.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("serial")
public class HandleDirectiveController {

    @Value("${server.port}")
    private int port;

    @GetMapping(value = "/{deviceId}/{hexs}")
    public void serialDataSend(@PathVariable String deviceId, @PathVariable List<String> hexs) throws Exception {
        System.out.println("port:" + port + ",deviceId:" + deviceId + ",hexs:" + hexs);
    }

}
