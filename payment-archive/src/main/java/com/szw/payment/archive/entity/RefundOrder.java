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
@Table("refund_order")
@Document("refund_order")
public class RefundOrder implements Serializable {
	@Serial
	private static final long serialVersionUID = 8450390300514885417L;

	/**
	 * id
	 */
	@Id
	@Column("id")
	private Long id;

	/**
	 * 支付单配置id
	 */
	@Column("config_id")
	@Field(value = "config_id", write = Field.Write.ALWAYS)
	private Long configId;

	/**
	 * 支付单id
	 */
	@Column("pay_order_id")
	@Field(value = "pay_order_id", write = Field.Write.ALWAYS)
	private Long payOrderId;

	/**
	 * 平台用户id
	 */
	@Column("user_id")
	@Field(value = "user_id", write = Field.Write.ALWAYS)
	private String userId;

	/**
	 * 平台订单id
	 */
	@Column("trade_id")
	@Field(value = "trade_id", write = Field.Write.ALWAYS)
	private String tradeId;

	/**
	 * api外部退款号，平台唯一
	 */
	@Column("out_refund_no")
	@Field(value = "out_refund_no", write = Field.Write.ALWAYS)
	private String outRefundNo;

	/**
	 * 退款金额
	 */
	@Column("amount")
	@Field(value = "amount", write = Field.Write.ALWAYS)
	private Long amount;

	/**
	 * 状态
	 */
	@Column("status")
	@Field(value = "status", write = Field.Write.ALWAYS)
	private Integer status;

	/**
	 * 幂等key 外部传入
	 */
	@Column("idempotent_key")
	@Field(value = "idempotent_key", write = Field.Write.ALWAYS)
	private String idempotentKey;

	/**
	 * 退款原因描述
	 */
	@Column("description")
	@Field(value = "description", write = Field.Write.ALWAYS)
	private String description;

	/**
	 * 退款创建时间
	 */
	@Column("refund_create_time")
	@Field(value = "refund_create_time", write = Field.Write.ALWAYS)
	private LocalDateTime refundCreateTime;

	/**
	 * 退款完成时间
	 */
	@Column("refund_end_time")
	@Field(value = "refund_end_time", write = Field.Write.ALWAYS)
	private LocalDateTime refundEndTime;

	/**
	 * 退款失败描述
	 */
	@Column("refund_fail_desc")
	@Field(value = "refund_fail_desc", write = Field.Write.ALWAYS)
	private String refundFailDesc;

	/**
	 * 重试次数
	 */
	@Column("retries")
	@Field(value = "retries", write = Field.Write.ALWAYS)
	private Integer retries;

	/**
	 * 回传参数
	 */
	@Column("pass_back_param")
	@Field(value = "pass_back_param", write = Field.Write.ALWAYS)
	private String passBackParam;

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
