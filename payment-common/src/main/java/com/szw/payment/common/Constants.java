package com.szw.payment.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public static final class Pay {
		/**
		 * 支付成功
		 */
		public static final String SUCCESS = "SUCCESS";

		/**
		 * 未支付
		 */
		public static final String NOT_PAY = "NOT_PAY";

		/**
		 * 已经关闭
		 */
		public static final String CLOSED = "CLOSED";

		/**
		 * 未知
		 */
		public static final String UNKNOWN = "UNKNOWN";
	}


	public static final class Refund {
		/**
		 * 退款成功
		 */
		public static final String SUCCESS = "SUCCESS";

		/**
		 * 退款失败
		 */
		public static final String ERROR = "ERROR";

		/**
		 * 退款中
		 */
		public static final String PROCESSING = "PROCESSING";
	}

}
