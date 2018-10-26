package com.oservice.mina.provider;

import com.oservice.mina.serial.handler.DataSendHandler;
import feign.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("directive")
public class HandleDirectiveController {

    @Resource
    private DataSendHandler dataSendHandler;

    @RequestMapping(value = "/data/send", method = RequestMethod.GET)
    public String serialDataSend(@Param("id") String id, @Param("data") byte[] data, @Param("remark") String remark) throws Exception {
        dataSendHandler.handleDirective(id,data);
        return "success";
    }

}
