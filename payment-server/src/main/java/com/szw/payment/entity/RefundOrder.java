package com.szw.payment.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("refund_order")
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
	private Long configId;

	/**
	 * 支付单id
	 */
	@Column("pay_order_id")
	private Long payOrderId;

	/**
	 * 平台用户id
	 */
	@Column("user_id")
	private String userId;

	/**
	 * 平台订单id
	 */
	@Column("trade_id")
	private String tradeId;

	/**
	 * api外部退款号，平台唯一
	 */
	@Column("out_refund_no")
	private String outRefundNo;

	/**
	 * 退款金额
	 */
	@Column("amount")
	private Long amount;

	/**
	 * 状态
	 */
	@Column("status")
	private Integer status;

	/**
	 * 幂等key 外部传入
	 */
	@Column("idempotent_key")
	private String idempotentKey;

	/**
	 * 退款原因描述
	 */
	@Column("description")
	private String description;

	/**
	 * 退款创建时间
	 */
	@Column("refund_create_time")
	private LocalDateTime refundCreateTime;

	/**
	 * 退款完成时间
	 */
	@Column("refund_end_time")
	private LocalDateTime refundEndTime;

	/**
	 * 退款失败描述
	 */
	@Column("refund_fail_desc")
	private String refundFailDesc;

	/**
	 * 重试次数
	 */
	@Column("retries")
	private Integer retries;

	/**
	 * 回传参数
	 */
	@Column("pass_back_param")
	private String passBackParam;

}
