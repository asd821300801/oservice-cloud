package com.oservice.cloud.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.oservice.cloud.service.feign"})
public class OserviceServiceBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(OserviceServiceBootstrap.class, args);
    }
}
