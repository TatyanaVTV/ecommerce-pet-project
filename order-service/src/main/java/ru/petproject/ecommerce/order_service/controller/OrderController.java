package ru.petproject.ecommerce.order_service.controller;

import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderDto;
import ru.petproject.ecommerce.order_service.service.order.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("/get/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId) {
        return orderService.findOrderById(orderId);
    }

    @GetMapping("/get/orders/by/{userId}")
    public List<OrderDto> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.findOrdersByUserId(userId);
    }

    @PostMapping("/create/new/{userId}")
    public OrderDto create(@PathVariable Long userId) {
        return orderService.createNewOrder(userId);
    }

    @PostMapping("/create/from/exist")
    public OrderDto create(@RequestBody OrderDto dto) {
        return orderService.createOrderFromExist(dto);
    }

    @PatchMapping("/update/total/cost/{orderId}")
    public OrderDto updateTotalCost(@PathVariable Long orderId, @RequestBody UpdateOrderDto updateOrderDto) {
        return orderService.updateOrderTotalCost(orderId, updateOrderDto);
    }

    @DeleteMapping("/delete/{orderId}")
    public void deleteById(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }


}
