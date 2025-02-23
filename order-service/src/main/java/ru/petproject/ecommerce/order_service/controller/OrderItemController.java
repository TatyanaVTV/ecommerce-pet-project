package ru.petproject.ecommerce.order_service.controller;

import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderItemDto;
import ru.petproject.ecommerce.order_service.service.orderitem.OrderItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderItems")
public class OrderItemController {

    private final OrderItemServiceImpl orderItemService;

    @GetMapping("/get/{orderItemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderItemId) {
        return orderItemService.findOrderItemById(orderItemId);
    }

    @GetMapping("/get/orderItems/by/{orderId}")
    public List<OrderItemDto> getOrderItemsByUserId(@PathVariable Long orderId) {
        return orderItemService.findOrderItemsByOrderId(orderId);
    }

    @PostMapping("/create/from/exist")
    public OrderItemDto create(@RequestBody OrderItemDto dto) {
        return orderItemService.createOrderItem(dto);
    }

    @PatchMapping("/update/total/cost/{orderItemId}")
    public OrderItemDto updateTotalCost(@PathVariable Long orderItemId, @RequestBody UpdateOrderItemDto updateOrderItemDto) {
        return orderItemService.updateOrderItem(orderItemId, updateOrderItemDto);
    }

    @DeleteMapping("/delete/{orderItemId}")
    public void deleteById(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
    }

}
