package com.mosaic.service.impl;

import com.mosaic.domain.request.OrderCreateRequest;
import com.mosaic.domain.response.OrderResponse;
import com.mosaic.entity.Order;
import com.mosaic.entity.OrderDetail;
import com.mosaic.entity.ProductVariant;
import com.mosaic.exception.ElementNotFoundException;
import com.mosaic.exception.InsufficientStockException;
import com.mosaic.mapper.OrderMapper;
import com.mosaic.repository.OrderDetailRepository;
import com.mosaic.repository.OrderRepository;
import com.mosaic.repository.ProductVariantRepository;
import com.mosaic.service.spec.CartService;
import com.mosaic.service.spec.OrderService;
import com.mosaic.service.spec.PaymentService;
import com.mosaic.service.spec.ProductVariantService;
import com.mosaic.util.constant.OrderStatusEnum;
import com.mosaic.util.constant.PaymentMethodEnum;
import com.mosaic.util.constant.PaymentStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartService cartService;
    private final ProductVariantService productVariantService;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;


    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        for (OrderCreateRequest.OrderDetailCreateRequest item : orderCreateRequest.getItems()) {
            ProductVariant productVariant = productVariantService.findProductVariantById(item.getProductVariantId());
            if(productVariant.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product variant: " + productVariant.getProduct().getName());
            }
        }

        Order order = Order.builder()
                .shippingPrice(orderCreateRequest.getShippingPrice())
                .totalAppliedItemsPrice(orderCreateRequest.getTotalAppliedItemsPrice())
                .totalOriginalItemsPrice(orderCreateRequest.getTotalOriginalItemsPrice())
                .totalPrice(orderCreateRequest.getTotalPrice())
                .status(OrderStatusEnum.ORDERED)
                .orderNumber(generateOrderNumber())
                .build();

        switch (orderCreateRequest.getPaymentMethod()) {
            case PaymentMethodEnum.BANK_TRANSFER:
                String referenceCode = createReferenceCode();
                order.setReferenceCode(referenceCode);
                order.setStatus(OrderStatusEnum.ORDERED);
                order = orderRepository.save(order);

                //Create payment
                paymentService.createPayment(orderCreateRequest.getUserId(), order,
                        referenceCode, orderCreateRequest.getTotalPrice(), PaymentStatusEnum.PENDING,
                        orderCreateRequest.getPaymentMethod());
                break;
            case PaymentMethodEnum.COD:
                order.setStatus(OrderStatusEnum.ORDERED);
                order = orderRepository.save(order);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + orderCreateRequest.getPaymentMethod());
        }


        final Order savedOrder = order;

        //Create order detail
        List<ProductVariant> variantsToUpdate = new ArrayList<>();
        List<OrderDetail> orderDetails = orderCreateRequest.getItems().stream()
                .map(item -> {
                    ProductVariant productVariant = productVariantService.findProductVariantById(item.getProductVariantId());
                    productVariant.setStockQuantity(productVariant.getStockQuantity() - item.getQuantity());
                    variantsToUpdate.add(productVariant);
                    return OrderDetail.builder()
                            .order(savedOrder)
                            .productVariant(productVariant)
                            .quantity(item.getQuantity())
                            .appliedUnitPrice(item.getAppliedPrice())
                            .originalUnitPrice(item.getOriginalPrice())
                            .build();
                }).toList();
        productVariantRepository.saveAll(variantsToUpdate);
        orderDetailRepository.saveAll(orderDetails);
        orderCreateRequest.getItems().forEach(item ->
                cartService.removeSpecificProductFromCart(orderCreateRequest.getUserId(), item.getProductVariantId()));
        return orderMapper.toOrderResponse(order);
    }

    private String createReferenceCode () {
        return UUID.randomUUID().toString();
    }

    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return "ORD-" + datePart + "-" + randomPart;
    }
    @Override
    public List<OrderResponse> findUserOrders(Long userId) {
        return orderMapper.toOrderResponseList(orderRepository.findByUserId(userId));
    }

    @Override
    public List<OrderResponse> findAllOrders() {
        return orderMapper.toOrderResponseList(orderRepository.findAll().stream()
                .filter(order -> !order.getIsDeleted()).toList());
    }

    @Override
    public OrderResponse findOrderResponseById(Long orderId) {
        return orderMapper.toOrderResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new ElementNotFoundException("Order not found!")));
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ElementNotFoundException("Order not found!"));
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusEnum status) {
        Order order = findOrderById(orderId);
        order.setStatus(status);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if(!order.getStatus().equals(OrderStatusEnum.ORDERED)) {
            throw new IllegalStateException("Cannot cancel order in status: " + order.getStatus());
        }
        for (OrderDetail detail : order.getDetails()) {
            ProductVariant productVariant = detail.getProductVariant();
            productVariant.setStockQuantity(productVariant.getStockQuantity() + detail.getQuantity());
            productVariantRepository.save(productVariant);
        }

        order.setStatus(OrderStatusEnum.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if(!order.getIsDeleted()) {
            order.setIsDeleted(true);
        }
    }
}
