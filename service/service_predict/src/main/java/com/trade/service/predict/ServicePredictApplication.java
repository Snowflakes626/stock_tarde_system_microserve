package com.trade.service.predict;

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
@MapperScan("com.trade.service.predict.mapper")
public class ServicePredictApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServicePredictApplication.class, args);
	}
}
