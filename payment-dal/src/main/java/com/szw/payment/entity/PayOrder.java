package com.szw.payment.entity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("pay_order")
public class PayOrder implements Serializable {

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
	private static final Type TYPE_OF_MAP = new TypeToken<Map<String, String>>() {}.getType();

	/**
	 * id
	 */
	@Id
	@Column("id")
	private Long id;

	/**
	 * 向支付平台申请支付单时使用的商家订单id
	 * 全局唯一
	 */
	@Column("out_trade_no")
	private String outTradeNo;

	/**
	 * 平台订单id
	 */
	@Column("trade_id")
	private String tradeId;

	/**
	 * 平台用户id
	 */
	@Column("user_id")
	private String userId;

	/**
	 * 支付配置id
	 */
	@Column("config_id")
	private Long configId;

	/**
	 * 支付渠道
	 */
	@Column("channel")
	private String channel;

	/**
	 * 支付平台返回预支付id
	 */
	@Column("pre_pay_id")
	private String prePayId;

	/**
	 * 支付流水号
	 */
	@Column("transaction_id")
	private String transactionId;

	/**
	 * 支付单支付金额
	 */
	@Column("amount")
	private Long amount;

	/**
	 * 余额（支付金额 - 退款金额 - 冻结金额）
	 */
	@Column("balance")
	private Long balance;

	/**
	 * 退款冻结金额
	 */
	@Column("refund_frozen_amount")
	private Long refundFrozenAmount;

	/**
	 * 已经退款金额
	 */
	@Column("refund_amount")
	private Long refundAmount;

	/**
	 * 状态
	 */
	@Column("status")
	private String status;

	/**
	 * 过期时间
	 */
	@Column("expire_time")
	private LocalDateTime expireTime;

	/**
	 * 支付完成时间
	 */
	@Column("pay_done_time")
	private LocalDateTime payDoneTime;

	/**
	 * 扩展字段
	 */
	@Column("extra_param")
	private String extraParam;

	/**
	 * 版本号
	 */
	@Version
	@Column("version")
	private Long version;

	@Transient
	private Map<String, String> extraParamMap;


	public PayOrder putExtraParam(String key, String val) {
		Map<String, String> map = getExtraParamMap();
		if (val != null) {
			map.put(key, val);
		}
		else {
			removeExtraParam(key);
		}
		return this;
	}

	public PayOrder removeExtraParam(String key) {
		if (key != null) {
			getExtraParamMap().remove(key);
		}
		return this;
	}

	public void generateExtraParam() {
		Map<String, String> extraParamMap = this.extraParamMap;
		if (extraParamMap != null) {
			this.extraParam = GSON.toJson(extraParamMap);
		}
	}

	public String getExtraParamByKey(String key) {
		return getExtraParamMap().get(key);
	}

	public String getExtraParamByKey(String key, String defaultValue) {
		String value = getExtraParamByKey(key);
		return Optional.ofNullable(value).orElse(defaultValue);
	}

	public Map<String, String> getExtraParamMap() {
		Map<String, String> extraParamMap = this.extraParamMap;
		if (extraParamMap == null) {
			initExtraParamByKeyIfNeeded();
		}
		// 需要返回 成员变量
		return this.extraParamMap;
	}

	private void initExtraParamByKeyIfNeeded() {
		Map<String, String> extraParamMap = this.extraParamMap;
		if (extraParamMap != null) {
			return;
		}

		String extraParam = this.extraParam;
		if (StringUtils.isBlank(extraParam)) {
			this.extraParamMap = new HashMap<>();
			return;
		}
		this.extraParamMap = GSON.fromJson(extraParam, TYPE_OF_MAP);
	}

}
