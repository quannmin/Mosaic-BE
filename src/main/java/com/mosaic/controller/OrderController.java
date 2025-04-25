package com.mosaic.controller;

import com.mosaic.domain.request.OrderCreateRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.OrderResponse;
import com.mosaic.service.spec.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Create order successfully!")
                        .code(HttpStatus.CREATED.value())
                        .data(orderService.createOrder(orderCreateRequest))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findAllOrders() {
        return ResponseEntity.ok(ApiResponse.<List<OrderResponse>>builder()
                        .data(orderService.findAllOrders())
                        .message("Get all orders successfully!")
                        .code(HttpStatus.OK.value())
                        .success(true)
                .build());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findAllOrdersByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.findUserOrders(id))
                .message("Get all orders successfully!")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> findOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder()
                .data(orderService.findOrderResponseById(id))
                .message("Get all orders successfully!")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Delete order successfully!")
                .code(HttpStatus.OK.value())
                .data(null)
                .build());
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete order successfully!")
                        .code(HttpStatus.OK.value())
                        .data(null)
                .build());
    }
}
