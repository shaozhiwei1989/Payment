package com.szw.payment.archive.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Archive implements Serializable {
	@Serial
	private static final long serialVersionUID = -6453006360907965029L;

	private PayOrder payOrder;

	private List<RefundOrder> refundOrderList;

}
