package com.szw.payment.archive.bootstrap;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.szw.payment.archive")
@EnableJdbcRepositories(basePackages = "com.szw.payment.archive.store.mysql")
@EnableMongoRepositories(basePackages = "com.szw.payment.archive.store.mongo")
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
