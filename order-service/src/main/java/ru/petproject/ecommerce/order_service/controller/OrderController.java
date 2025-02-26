package ru.petproject.ecommerce.order_service.controller;

import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.dto.OrderItemDtoWithoutOrderId;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderItemDto;
import ru.petproject.ecommerce.order_service.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("/get/order/by/userId")
    public List<OrderDto> findOrdersByUserId(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        return orderService.findOrdersByUserId(token);
    }

    @GetMapping("/get/orders/by/to/status/new")
    public OrderDto findOrdersByUserIdAndStatusNew(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        return orderService.findOrdersWithStatusNewByUserId(token);
    }

    @GetMapping("/get/order/item/by/{orderItemId}")
    public OrderItemDto findOrderItemById(@PathVariable Long orderItemId) {
        return orderService.findOrderItemById(orderItemId);
    }

    @GetMapping("/get/order/by/{orderId}")
    public List<OrderItemDto> findOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.findOrderItemsByOrderId(orderId);
    }

    @PostMapping("/add/order/item")
    public OrderDto addOrderItemToOrder(@RequestHeader("Authorization") String token,
                                        @RequestBody OrderItemDtoWithoutOrderId dto) {
        token = token.replace("Bearer ", "");
        return orderService.addOrderItemToOrder(token, dto);
    }

    @DeleteMapping("/delete/order/by/{orderId}")
    public void deleteOrderById(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @PatchMapping("/change/quantity/{orderItemId}")
    public OrderItemDto changeOrderItemQuantity (@PathVariable Long orderItemId, @RequestBody UpdateOrderItemDto dto) {
        return orderService.changeOrderItemQuantity(orderItemId, dto);
    }

    @DeleteMapping("/delete/order/item/{orderItemId}")
    public void removeOrderItemFromOrder(@PathVariable Long orderItemId) {
        orderService.deleteOrderItem(orderItemId);
    }

    @PatchMapping("/place/order/{orderId}")
    public OrderDto placeOrder (@PathVariable Long orderId) {
        return orderService.placeOrder(orderId);
    }
}
