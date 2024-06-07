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
@Table("refund_task")
public class RefundTask implements Serializable {
	@Serial
	private static final long serialVersionUID = 1723491984763145486L;

	/**
	 * id
	 */
	@Id
	@Column("id")
	private Long id;

	/**
	 * 退款单id
	 */
	@Column("refund_order_id")
	private Long refundOrderId;

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
	 * 执行时间
	 */
	@Column("exec_time")
	private LocalDateTime execTime;

}
