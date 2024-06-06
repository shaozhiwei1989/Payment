package com.szw.payment.test;

import java.util.List;

import com.szw.payment.bootstrap.Application;
import com.szw.payment.entity.Config;
import com.szw.payment.store.ConfigStore;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class ConfigStoreTest {

	@Inject
	private ConfigStore configStore;


	@Test
	public void findByChannel() {
		List<Config> list = configStore.findByChannel("wx_app");
		System.out.print(list + "\n");
	}

	@Test
	public void testFindById() {
		Config config = configStore.findById(1L);
		System.out.print(config + "\n");
	}

}
