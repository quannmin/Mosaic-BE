package com.mosaic.mapper;

import com.mosaic.domain.request.OrderCreateRequest;
import com.mosaic.domain.response.OrderResponse;
import com.mosaic.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    public OrderResponse toOrderResponse(Order order);
    public Order toOrder(OrderCreateRequest orderCreateRequest);
    public List<OrderResponse> toOrderResponseList(List<Order> orders);
}
