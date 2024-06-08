package com.szw.payment.test;

import java.time.LocalDateTime;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.manager.PayOrderManager;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class Test1 {

	@Inject
	private PayOrderManager payOrderManager;

	@Test
	public void testCompletePay() {
		payOrderManager.completePay("tradeid_10000-1", "xxxxxxx", LocalDateTime.now());
	}

}
