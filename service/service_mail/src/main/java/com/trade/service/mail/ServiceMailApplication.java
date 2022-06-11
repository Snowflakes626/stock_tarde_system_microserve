package com.trade.service.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.trade"})
public class ServiceMailApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceMailApplication.class, args);
	}
}
