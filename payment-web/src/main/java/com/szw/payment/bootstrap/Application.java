package com.szw.payment.bootstrap;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.szw.payment")
@EnableDubbo(scanBasePackages = "com.szw.payment.controller")
public class Application {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		SpringApplication.run(Application.class, args);

		long end = System.currentTimeMillis();
		System.out.print("---started--- time:" + (end - start) + "ms \n");
	}

}
