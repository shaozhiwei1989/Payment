package com.szw.payment.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public static final class Pay {
		/**
		 * 支付成功
		 */
		public static final String SUCCESS = "success";

		/**
		 * 未支付
		 */
		public static final String WAIT_PAY = "wait_pay";

		/**
		 * 已经关闭
		 */
		public static final String CLOSED = "closed";

		/**
		 * 未知
		 */
		public static final String UNKNOWN = "unknown";
	}


	public static final class Refund {
		/**
		 * 退款成功
		 */
		public static final String SUCCESS = "success";

		/**
		 * 退款失败
		 */
		public static final String ERROR = "error";

		/**
		 * 退款中
		 */
		public static final String PROCESSING = "processing";
	}

	public static final class Tag {
		public static final String KEY = "tag";

		public static final String SUCCESS = "success";

		public static final String ERROR = "error";

	}

}
