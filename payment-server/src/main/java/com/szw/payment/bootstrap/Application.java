package com.szw.payment.bootstrap;

import java.util.concurrent.CountDownLatch;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.szw.payment")
@EnableDubbo(scanBasePackages = "com.szw.payment.service")
@EnableJdbcRepositories(basePackages = "com.szw.payment.store")
public class Application {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SpringApplication.run(Application.class, args);
		long end = System.currentTimeMillis();

		System.out.print("---started--- time:" + (end - start) + "ms \n");
		CountDownLatch latch = new CountDownLatch(1);
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			System.exit(0);
		}
	}
}
