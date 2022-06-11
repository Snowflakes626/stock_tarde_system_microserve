package com.trade.service.domestic.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.trade"})
@EnableDiscoveryClient
@MapperScan("com.trade.service.domestic.stock.mapper")
public class ServiceDomesticStockApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceDomesticStockApplication.class, args);
	}
}
