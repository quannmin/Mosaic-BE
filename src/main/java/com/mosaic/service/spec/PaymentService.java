package com.mosaic.service.spec;

import com.mosaic.entity.Order;
import com.mosaic.entity.Payment;
import com.mosaic.util.constant.PaymentMethodEnum;
import com.mosaic.util.constant.PaymentStatusEnum;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
     Payment createPayment(Long userId, Order order, String referenceCode, BigDecimal amount, PaymentStatusEnum status, PaymentMethodEnum paymentMethod);
     Payment getPayment(Long id);
     Payment updatePaymentStatus(Long id, PaymentStatusEnum paymentStatus);
     void deletePayment(Long id);
     Payment getPaymentByReferenceCode(String referenceCode);
     List<Payment> getAllPayments();
}
