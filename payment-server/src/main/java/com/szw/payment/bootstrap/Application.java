package com.szw.payment.bootstrap;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.szw.payment")
@EnableJdbcRepositories(basePackages = "com.szw.payment.store")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		System.out.print("------------started--------- \n");
		CountDownLatch latch = new CountDownLatch(1);
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			System.exit(0);
		}
	}
}
