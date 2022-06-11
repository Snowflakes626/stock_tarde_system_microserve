package com.trade.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan({"com.trade"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.trade.service.user.mapper")
public class ServiceUserApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceUserApplication.class, args);
	}
}
