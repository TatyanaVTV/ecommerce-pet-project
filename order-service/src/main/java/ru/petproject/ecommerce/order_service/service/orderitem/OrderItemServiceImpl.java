package ru.petproject.ecommerce.order_service.service.orderitem;

import org.springframework.kafka.core.KafkaTemplate;
import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderItemDto;
import ru.petproject.ecommerce.order_service.exception.OrderItemNotFoundException;
import ru.petproject.ecommerce.order_service.exception.OrderNotFoundException;
import ru.petproject.ecommerce.order_service.mapper.OrderItemMapper;
import ru.petproject.ecommerce.order_service.model.Order;
import ru.petproject.ecommerce.order_service.model.OrderItem;
import ru.petproject.ecommerce.order_service.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.petproject.ecommerce.order_service.repository.OrderRepository;
import ru.petproject.ecommerce.order_service.service.order.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Override
    public OrderItemDto findOrderItemById(Long orderItemId) {
        log.info("Received orderItem by id: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("orderItem didn't find"));
        log.info("OrderItem {} received successfully", orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndDeletedFalse(orderId);
        return orderItems.stream().map(orderItemMapper::toDto).collect(Collectors.toList());
    }



    @Override
    @Transactional
    public OrderItemDto createOrderItem(OrderItemDto dto) {
        log.info("Creating orderItem from exist orderItem's data: {}", dto);
        Optional<Order> order = orderRepository.findByIdAndDeletedFalse(dto.getOrderId());
        if (orderRepository.existsById(dto.getOrderId()) && order.isPresent()) {
            OrderItem orderItem = OrderItem.builder()
                    .orderId(dto.getOrderId())
                    .productId(dto.getProductId())
                    .quantity(dto.getQuantity())
                    .price(dto.getPrice())
                    .build();
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            BigDecimal orderItemTotalPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderService.updateOrderTotalCost(dto.getOrderId(), orderItemTotalPrice);

            log.info("OrderItem successfully created: {}", orderItem);

            return orderItemMapper.toDto(savedOrderItem);

        }
        else throw new OrderNotFoundException("Order not found");
    }

    @Override
    @Transactional
    public OrderItemDto updateOrderItem(Long orderItemId, UpdateOrderItemDto updateOrderItemDto) {
        log.info("Updating quantity of orderItem: {}", orderItemId);

        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("orderItem doesn't find"));

        Integer prevQuantity = orderItem.getQuantity();
        orderItem.setQuantity(updateOrderItemDto.getQuantity());
        Integer currentQuantity = orderItem.getQuantity();
        int difference = currentQuantity - prevQuantity;
        orderItemRepository.save(orderItem);

        BigDecimal orderItemTotalPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(difference));
        orderService.updateOrderTotalCost(updateOrderItemDto.getOrderId(), orderItemTotalPrice);

        log.info("OrderItem updated successfully: {}", orderItem);
        return orderItemMapper.toDto(orderItem);
    }


    @Transactional
    @Override
    public void deleteOrderItem(Long orderItemId) {
        log.info("Deleting orderItem: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("orderItem doesn't find"));

        orderItem.setDeleted(true);
        orderItemRepository.save(orderItem);

        BigDecimal orderItemTotalPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        orderService.updateOrderTotalCost(orderItem.getOrderId(), orderItemTotalPrice);

        log.info("OrderItem deleted successfully: {}", orderItem);
    }
}
