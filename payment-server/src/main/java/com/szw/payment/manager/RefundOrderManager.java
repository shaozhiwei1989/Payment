package com.szw.payment.manager;

import java.time.LocalDateTime;
import java.util.Optional;

import com.szw.payment.common.Constants;
import com.szw.payment.common.RefundStatusEnum;
import com.szw.payment.common.model.Refund;
import com.szw.payment.common.model.RefundOrderMessage;
import com.szw.payment.converter.Converter;
import com.szw.payment.entity.PayOrder;
import com.szw.payment.entity.RefundOrder;
import com.szw.payment.entity.RefundTask;
import com.szw.payment.exception.NotEnoughAmountException;
import com.szw.payment.exception.UpdateDataException;
import com.szw.payment.producer.MessageProducer;
import com.szw.payment.store.PayOrderStore;
import com.szw.payment.store.RefundOrderStore;
import com.szw.payment.store.RefundTaskStore;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Named
@Slf4j(topic = "running")
public class RefundOrderManager {

	@Inject
	private PayOrderStore payOrderStore;

	@Inject
	private RefundOrderStore refundOrderStore;

	@Inject
	private RefundTaskStore refundTaskStore;

	@Value("#{${message-producer.name}}")
	private MessageProducer<RefundOrderMessage> messageProducer;

	@Value("${mq-topic.refund}")
	private String topic;


	public RefundOrder findById(Long id) {
		return refundOrderStore.findById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public void createRefundOrder(PayOrder payOrder, Refund refund) {
		int i = payOrderStore.freezeRefundAmount(payOrder.getId(), refund.getRefundFee());
		if (i > 0) {
			RefundOrder refundOrder = Converter.buildRefundOrder(payOrder, refund);
			refundOrderStore.save(refundOrder);

			RefundTask refundTask = Converter.buildRefundTask(refundOrder);
			refundTaskStore.save(refundTask);
		}
		if (i == 0) {
			throw new NotEnoughAmountException(
					String.format("余额不足 支付单余额:%s 申请退款金额:%s",
							payOrder.getBalance(), refund.getRefundFee()));
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void completeRefund(String outRefundNo, LocalDateTime doneTime, boolean success, String failDesc) {
		RefundOrder refundOrder = refundOrderStore.findByOutRefundNo(outRefundNo);
		if (refundOrder == null || !checkStatusForCompleteRefund(refundOrder)) {
			return;
		}

		Long amount = refundOrder.getAmount();
		Integer fromStatus = refundOrder.getStatus();
		Long payOrderId = refundOrder.getPayOrderId();
		LocalDateTime endTime = Optional.ofNullable(doneTime).orElse(LocalDateTime.now());

		int i; // 数据库更新行数
		if (success) {
			i = payOrderStore.completeRefund(payOrderId, amount);
			if (i > 0) {
				refundOrder.setRefundFailDesc(null);
				refundOrder.setRefundEndTime(endTime);
				refundOrder.setStatus(RefundStatusEnum.REFUND_SUCCESS.getCode());
				i = refundOrderStore.updateRefundToComplete(refundOrder, fromStatus);
			}
		}
		else {
			i = payOrderStore.rollbackRefund(payOrderId, amount);
			if (i > 0) {
				refundOrder.setRefundEndTime(endTime);
				refundOrder.setRefundFailDesc(failDesc);
				refundOrder.setStatus(RefundStatusEnum.REFUND_ERR.getCode());
				i = refundOrderStore.updateRefundToComplete(refundOrder, fromStatus);
			}
		}

		if (i == 0) {
			throw new UpdateDataException("数据更新异常#outRefundNo:" + outRefundNo);
		}

		afterCompleteRefund(refundOrder);
	}

	private void afterCompleteRefund(RefundOrder refundOrder) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				RefundStatusEnum statusEnum = RefundStatusEnum.fromCode(refundOrder.getStatus());
				boolean success = (statusEnum == RefundStatusEnum.REFUND_SUCCESS);

				String tag = success ? Constants.Tag.SUCCESS : Constants.Tag.ERROR;
				RefundOrderMessage message = Converter.buildRefundOrderMessage(refundOrder);
				messageProducer.send(topic, tag, message);

				refundTaskStore.deleteByRefundOrderId(refundOrder.getId());
			}
		});
	}

	public int countByIdempotentKey(String idempotentKey) {
		return refundOrderStore.countByIdempotentKey(idempotentKey);
	}

	public RefundOrder findByIdempotentKey(String idempotentKey) {
		return refundOrderStore.findByIdempotentKey(idempotentKey);
	}

	public void updateRefundToRetry(RefundOrder refundOrder, String failDesc, int retries) {
		int i = refundOrderStore.updateRefundToRetry(refundOrder.getId(),
				refundOrder.getStatus(), RefundStatusEnum.WAITING_RETRY.getCode(),
				failDesc, retries);
		if (i == 0) {
			throw new UpdateDataException("数据更新异常#refundOrder:" + refundOrder);
		}
	}

	public void updateRefundToIng(RefundOrder refundOrder) {
		int i = refundOrderStore.updateRefundToIng(refundOrder.getId(),
				refundOrder.getStatus(), RefundStatusEnum.REFUND_ING.getCode());
		if (i == 0) {
			throw new UpdateDataException("数据更新异常#refundOrder:" + refundOrder);
		}
	}

	private boolean checkStatusForCompleteRefund(RefundOrder refundOrder) {
		RefundStatusEnum statusEnum = RefundStatusEnum.fromCode(refundOrder.getStatus());
		return statusEnum == RefundStatusEnum.INIT
				|| statusEnum == RefundStatusEnum.WAITING_RETRY
				|| statusEnum == RefundStatusEnum.REFUND_ING;
	}

}
