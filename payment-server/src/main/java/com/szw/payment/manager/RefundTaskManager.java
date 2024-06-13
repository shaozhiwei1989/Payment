package com.szw.payment.manager;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import com.szw.payment.common.Constants;
import com.szw.payment.common.RefundStatusEnum;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundCreateResponse;
import com.szw.payment.common.model.RefundQueryResponse;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.Config;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.entity.RefundOrder;
import com.szw.payment.entity.RefundTask;
import com.szw.payment.facade.PayFacade;
import com.szw.payment.sdk.PayExceptionHandler;
import com.szw.payment.sdk.exception.PayException;
import com.szw.payment.store.ConfigStore;
import com.szw.payment.store.PayOrderStore;
import com.szw.payment.store.RefundTaskStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j(topic = "running")
public class RefundTaskManager {
	private final static int[] time_interval = new int[] {1, 1, 2, 2, 5, 10, 20, 30, 60};

	@Inject
	private PayOrderStore payOrderStore;

	@Inject
	private ConfigStore configStore;

	@Inject
	private RefundTaskStore refundTaskStore;

	@Inject
	private RefundOrderManager refundOrderManager;

	@Inject
	private PayFacade payFacade;


	public void executeRefundTask(RefundTask refundTask) {
		RefundOrder refundOrder = refundOrderManager.findById(refundTask.getRefundOrderId());
		RefundStatusEnum statusEnum = RefundStatusEnum.fromCode(refundOrder.getStatus());
		switch (statusEnum) {
			case INIT, WAITING_RETRY -> applyRefund(refundOrder);
			case REFUND_ING -> completeRefundIfPossible(refundOrder);
			default -> refundTaskStore.deleteById(refundTask.getId());
		}
	}

	private void applyRefund(RefundOrder refundOrder) {
		Config config = configStore.findById(refundOrder.getConfigId());
		PayOrder payOrder = payOrderStore.findById(refundOrder.getPayOrderId());

		Refund refund = Converter.buildRefund(payOrder, refundOrder);
		RefundExceptionHandler handler = new RefundExceptionHandler(refundOrder);
		RefundCreateResponse response = payFacade.createRefund(config, handler, refund);
		if (response == null) {
			return;
		}

		refundOrderManager.updateRefundToIng(refundOrder);

		// 无需回调时，默认成功 直接完成退款
		if (!response.isWaitCallBack()) {
			refundOrderManager.completeRefund(refundOrder.getOutRefundNo(),
					LocalDateTime.now(), true, null);
		}
	}

	private void completeRefundIfPossible(RefundOrder refundOrder) {
		Config config = configStore.findById(refundOrder.getConfigId());
		PayOrder payOrder = payOrderStore.findById(refundOrder.getPayOrderId());

		Refund refund = Converter.buildRefund(payOrder, refundOrder);
		RefundQueryResponse response = payFacade.queryRefundOrder(config, refund);
		if (response == null || Constants.Refund.PROCESSING.equals(response.getRefundStatus())) {
			log.info("查询结果为空 或 退款单处于退款中本次不处理#tradeId:{}", refundOrder.getTradeId());
		}
		else {
			boolean success = Constants.Refund.SUCCESS.equals(response.getRefundStatus());
			refundOrderManager.completeRefund(refundOrder.getOutRefundNo(),
					response.getRefundDoneTime(), success, null);
		}
	}

	@RequiredArgsConstructor
	private class RefundExceptionHandler implements PayExceptionHandler {

		private final RefundOrder refundOrder;

		@Override
		public boolean onException(Method method, PayException exception) {
			if (!"createRefund".equals(method.getName())) {
				// 非申请退款方法 不处理
				return true;
			}

			int maxRetries = 30;
			int retries = Optional.ofNullable(refundOrder.getRetries()).orElse(0); ;
			if (exception.isRetryLater() && retries < maxRetries) {
				refundOrderManager.updateRefundToRetry(refundOrder, exception.getMessage(), (retries + 1));

				int interval = time_interval[Math.min(time_interval.length - 1, retries)];
				LocalDateTime execTime = LocalDateTime.now().plusMinutes(interval);
				refundTaskStore.updateExecTime(refundOrder.getId(), execTime);
			}
			else {
				// 重试最大次数 退款失败
				refundOrderManager.completeRefund(refundOrder.getOutRefundNo(),
						LocalDateTime.now(), false, exception.getMessage());
			}
			return false;
		}
	}

}
