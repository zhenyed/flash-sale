package io.github.zhenyed.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = {"io.github.zhenyed.api.common.service"})
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@ComponentScan(basePackages = {"io.github.zhenyed.user", "io.github.zhenyed.api"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
