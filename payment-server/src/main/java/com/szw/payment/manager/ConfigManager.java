package com.szw.payment.manager;

import java.util.List;
import java.util.Random;

import com.szw.payment.entity.Config;
import com.szw.payment.store.ConfigStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class ConfigManager {

	@Inject
	private ConfigStore configStore;


	public Config findOneByChannel(String channel) {
		List<Config> list = configStore.findByChannel(channel);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("缺少对应的支付配置");
		}
		if (list.size() == 1) {
			return list.getFirst();
		}
		int index = new Random().nextInt(list.size());
		return list.get(index); // 随机返回一个
	}

	public Config findById(Long id) {
		return configStore.findById(id);
	}

}
