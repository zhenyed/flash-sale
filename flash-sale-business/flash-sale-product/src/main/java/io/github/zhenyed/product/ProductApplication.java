package io.github.zhenyed.product;

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
@ComponentScan(basePackages = {"io.github.zhenyed.product", "io.github.zhenyed.api"})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
