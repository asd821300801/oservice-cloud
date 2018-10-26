package com.oservice.mina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.oservice.mina.feign"})
@SpringBootApplication
public class OserviceMinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OserviceMinaApplication.class, args);
    }
}
