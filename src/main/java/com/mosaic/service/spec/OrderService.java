package com.mosaic.service.spec;

import com.mosaic.domain.request.OrderCreateRequest;
import com.mosaic.domain.request.OrderUpdateRequest;
import com.mosaic.domain.response.OrderResponse;
import com.mosaic.entity.Order;
import com.mosaic.util.constant.OrderStatusEnum;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrder(Long orderId, OrderUpdateRequest orderUpdateRequest);
    List<OrderResponse> findUserOrders(Long userId);
    List<OrderResponse> findAllOrders();
    OrderResponse findOrderResponseById(Long orderId);
    Order findOrderById(Long orderId);
    void updateOrderStatus(Long orderId, OrderStatusEnum status);
    void cancelOrder(Long orderId);
    void deleteOrder(Long orderId);
}
