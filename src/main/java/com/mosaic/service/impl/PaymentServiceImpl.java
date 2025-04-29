package com.mosaic.service.impl;

import com.mosaic.entity.Order;
import com.mosaic.entity.Payment;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.repository.PaymentRepository;
import com.mosaic.service.spec.PaymentService;
import com.mosaic.service.spec.UserService;
import com.mosaic.util.constant.PaymentMethodEnum;
import com.mosaic.util.constant.PaymentStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Override
    public Payment createPayment(Long userId, Order order, String referenceCode, BigDecimal amount, PaymentStatusEnum status, PaymentMethodEnum paymentMethod) {
        User user = userService.findUserById(userId);
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setReferenceCode(referenceCode);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setPaymentMethod(paymentMethod);
        payment.setOrder(order);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Payment not found!",
                "id",
                id.toString())
        ) ;
    }

    @Override
    public Payment updatePaymentStatus(Long id, PaymentStatusEnum paymentStatus) {
        Payment payment = getPayment(id);
        payment.setStatus(paymentStatus);
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = getPayment(id);
        if(!payment.getIsDeleted()) {
            payment.setIsDeleted(true);
        }
        paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByReferenceCode(String referenceCode) {
        return paymentRepository.findByReferenceCode(referenceCode).orElseThrow(() -> new ResourceNotFoundException(
                "Payment not found!",
                "referenceCode",
                referenceCode
        ));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll().stream().filter(payment -> !payment.getIsDeleted()).toList();
    }
}
