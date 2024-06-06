package com.szw.payment.store;

import java.util.List;

import com.szw.payment.entity.Config;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface ConfigStore extends Repository<Config, Long> {

	@Query("select * from config where channel = :channel and is_deleted = 0")
	List<Config> findByChannel(String channel);

	Config findById(Long id);

}
