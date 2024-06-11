package com.szw.payment.archive.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("pay_order")
@Document("pay_order")
public class PayOrder implements Serializable {
	@Serial
	private static final long serialVersionUID = 8667123945785343517L;

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
	@Field(value = "out_trade_no", write = Field.Write.ALWAYS)
	private String outTradeNo;

	/**
	 * 平台订单id
	 */

	@Column("trade_id")
	@Field(value = "trade_id", write = Field.Write.ALWAYS)
	private String tradeId;

	/**
	 * 平台用户id
	 */
	@Column("user_id")
	@Field(value = "user_id", write = Field.Write.ALWAYS)
	private String userId;

	/**
	 * 支付配置id
	 */
	@Column("config_id")
	@Field(value = "config_id", write = Field.Write.ALWAYS)
	private Long configId;

	/**
	 * 支付渠道
	 */
	@Column("channel")
	@Field(value = "channel", write = Field.Write.ALWAYS)
	private String channel;

	/**
	 * 支付平台返回预支付id
	 */
	@Column("pre_pay_id")
	@Field(value = "pre_pay_id", write = Field.Write.ALWAYS)
	private String prePayId;

	/**
	 * 支付流水号
	 */
	@Column("transaction_id")
	@Field(value = "transaction_id", write = Field.Write.ALWAYS)
	private String transactionId;

	/**
	 * 支付单支付金额
	 */
	@Column("amount")
	@Field(value = "amount", write = Field.Write.ALWAYS)
	private Long amount;

	/**
	 * 余额（支付金额 - 退款金额 - 冻结金额）
	 */
	@Column("balance")
	@Field(value = "balance", write = Field.Write.ALWAYS)
	private Long balance;

	/**
	 * 退款冻结金额
	 */
	@Column("refund_frozen_amount")
	@Field(value = "refund_frozen_amount", write = Field.Write.ALWAYS)
	private Long refundFrozenAmount;

	/**
	 * 已经退款金额
	 */
	@Column("refund_amount")
	@Field(value = "refund_amount", write = Field.Write.ALWAYS)
	private Long refundAmount;

	/**
	 * 状态
	 */
	@Column("status")
	@Field(value = "status", write = Field.Write.ALWAYS)
	private String status;

	/**
	 * 过期时间
	 */
	@Column("expire_time")
	@Field(value = "expire_time", write = Field.Write.ALWAYS)
	private LocalDateTime expireTime;

	/**
	 * 支付完成时间
	 */
	@Column("pay_done_time")
	@Field(value = "pay_done_time", write = Field.Write.ALWAYS)
	private LocalDateTime payDoneTime;

	/**
	 * 扩展字段
	 */
	@Column("extra_param")
	@Field(value = "extra_param", write = Field.Write.ALWAYS)
	private String extraParam;

	/**
	 * 创建时间
	 */
	@Column("create_time")
	@Field(value = "create_time", write = Field.Write.ALWAYS)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Column("update_time")
	@Field(value = "update_time", write = Field.Write.ALWAYS)
	private LocalDateTime updateTime;

}
