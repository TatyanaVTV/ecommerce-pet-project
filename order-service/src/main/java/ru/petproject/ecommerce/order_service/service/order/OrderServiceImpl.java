package ru.petproject.ecommerce.order_service.service.order;

import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderDto;
import ru.petproject.ecommerce.order_service.exception.OrderNotFoundException;
import ru.petproject.ecommerce.order_service.mapper.OrderMapper;
import ru.petproject.ecommerce.order_service.model.Order;
import ru.petproject.ecommerce.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    public static final BigDecimal START_TOTAL_COST = new BigDecimal("0.00");
    public static final String START_PAYMENT_METHOD = "SBPFake";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public OrderDto findOrderById(Long orderId) {
        log.info("Received order by id: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        log.info("Order received successfully: {}", order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndDeletedFalse(userId);
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto createNewOrder(Long userId) {
        log.info("Creating new order");
        Order order = Order.builder()
                .userId(userId)
                .status(Order.Status.NEW)
                .totalCost(START_TOTAL_COST)
                .paymentMethod(START_PAYMENT_METHOD)
                .build();
        Order savedOrder = orderRepository.save(order);
        log.info("New order successfully created: {}", order);

        //kafkaTemplate.send("product-created-events-topic", order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto createOrderFromExist(OrderDto dto) {
        log.info("Creating order from exist order's data: {}", dto);
        Order order = Order.builder()
                .userId(dto.getUserId())
                .totalCost(dto.getTotalCost())
                .paymentMethod(dto.getPaymentMethod())
                .build();
        Order savedOrder = orderRepository.save(order);
        log.info("Order successfully created: {}", order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto updateOrderTotalCost(Long orderId, UpdateOrderDto updateOrderDto) {
        log.info("Updating total cost of order: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setTotalCost(updateOrderDto.getTotalCost());
        orderRepository.save(order);
        log.info("Order updated successfully: {}", order);
        return orderMapper.toDto(order);
    }
    @Override
    @Transactional
    public OrderDto updateOrderTotalCost(Long orderId, BigDecimal totalCost) {
        log.info("Updating total cost of order: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setTotalCost(order.getTotalCost().add(totalCost));
        orderRepository.save(order);
        log.info("Order updated successfully: {}", order);
        return orderMapper.toDto(order);
    }



    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        log.info("Deleting order: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setDeleted(true);
        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);
        log.info("Order deleted successfully: {}", order);
    }
}
