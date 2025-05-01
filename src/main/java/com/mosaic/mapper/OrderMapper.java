package com.mosaic.mapper;

import com.mosaic.domain.request.OrderCreateRequest;
import com.mosaic.domain.request.OrderUpdateRequest;
import com.mosaic.domain.response.OrderResponse;
import com.mosaic.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
    Order toOrder(OrderCreateRequest orderCreateRequest);
    List<OrderResponse> toOrderResponseList(List<Order> orders);
    void toUpdateOrderResponse(OrderUpdateRequest orderUpdateRequest, @MappingTarget Order order);
}
