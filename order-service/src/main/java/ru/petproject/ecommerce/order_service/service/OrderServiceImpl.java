package ru.petproject.ecommerce.order_service.service;

import lombok.ToString;
import ru.petproject.ecommerce.order_service.dto.*;
import ru.petproject.ecommerce.order_service.exception.OrderItemNotFoundException;
import ru.petproject.ecommerce.order_service.exception.OrderNotFoundException;
import ru.petproject.ecommerce.order_service.jwt.JwtTokenProvider;
import ru.petproject.ecommerce.order_service.mapper.OrderItemMapper;
import ru.petproject.ecommerce.order_service.mapper.OrderMapper;
import ru.petproject.ecommerce.order_service.model.Order;
import ru.petproject.ecommerce.order_service.model.OrderItem;
import ru.petproject.ecommerce.order_service.model.Status;
import ru.petproject.ecommerce.order_service.repository.OrderItemRepository;
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
@ToString
public class  OrderServiceImpl implements OrderService {
    public static final BigDecimal START_TOTAL_COST = new BigDecimal("0.00");
    public static final String START_PAYMENT_METHOD = "SBPFake";
    public static final String ORDER_TOPIC = "order-topic";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Override
    public OrderDto findOrderById(Long orderId) {
        log.info("Receiving order by id: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setTotalCost(getTotalCostForNewOrder(order));
        log.info("Order received successfully: {}", order.getId());
        return orderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> findOrdersByUserId(String token) {
        log.info("Receiving orders by token: {}", token);
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        List<Order> orders = orderRepository.findByUserIdAndDeletedFalse(userId);
        for (Order order : orders) {
            order.setTotalCost(getTotalCostForNewOrder(order));
        }
        log.info("Orders received successfully: {}", orders);
        return orders.stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto findOrdersWithStatusNewByUserId(String token) {
        log.info("Receiving order with status NEW by token: {}", token);
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        Order order = orderRepository.findByUserIdAndDeletedFalseAndStatusEquals(userId, Status.NEW);
        order.setTotalCost(getTotalCostForNewOrder(order));
        log.info("Order received successfully: {}",order.getId());
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderItemId) {
        log.info("Received orderItem by id: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("orderItem didn't find"));
        log.info("OrderItem {} received successfully", orderItem.getId());
        return orderItemMapper.toOrderItemDto(orderItem);
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(Long orderId) {
        log.info("Receiving order items by order id: {}", orderId);
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndDeletedFalse(orderId);
        log.info("Order items received successfully: {}", orderItems.toString());
        return orderItems.stream().map(orderItemMapper::toOrderItemDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDto addOrderItemToOrder(String token, OrderItemDtoWithoutOrderId dto) {
            log.info("Trying to add order item to the order: {}", dto.toString());
            Long userid = jwtTokenProvider.getUserIdFromJWT(token);
            Order order = orderRepository.findByUserIdAndDeletedFalseAndStatusEquals(userid, Status.NEW);
            if (order == null) {
                order = createNewOrder(userid);
            }
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(dto.getProductId())
                    .quantity(dto.getQuantity())
                    .price(dto.getPrice())
                    .deleted(false)
                    .build();
            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
            order.setTotalCost(getTotalCostForNewOrder(order));
            log.info("Order item successfully added to the order: {}", order.getId());
            return orderMapper.toOrderDto(order);

    }

    @Transactional
    @Override
    public Order createNewOrder(Long userid) {
        log.info("Creating new order");
        Order order = Order.builder()
                .userId(userid)
                .status(Status.NEW)
                .totalCost(START_TOTAL_COST)
                .paymentMethod(START_PAYMENT_METHOD)
                .build();
        Order savedOrder = orderRepository.save(order);
        log.info("New order successfully created: {}", order.getId());
        order.setTotalCost(getTotalCostForNewOrder(order));
        return savedOrder;
    }

    BigDecimal getTotalCostForNewOrder (Order order) {
        BigDecimal orderItemTotalCost = new BigDecimal("0.00");
        OrderDto orderDto = orderMapper.toOrderDto(order);
        for (OrderItemDto dto: orderDto.getItems()) {
            if(!dto.isDeleted()){
                orderItemTotalCost = orderItemTotalCost.add(dto.getPrice()
                        .multiply(BigDecimal.valueOf(dto.getQuantity())));
            }
        }
        log.info("Received total cost {} of New order", orderItemTotalCost);
        return orderItemTotalCost;
    }

    @Transactional
    @Override
    public OrderItemDto changeOrderItemQuantity(Long orderItemId, UpdateOrderItemDto updateOrderItemDto) {
        log.info("Changing quantity of order item: {}", orderItemId);

        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item didn't find"));

        orderItem.setQuantity(updateOrderItemDto.getQuantity());
        orderItemRepository.save(orderItem);

        log.info("Changed quantity of order item: {}", orderItemId);
        return orderItemMapper.toOrderItemDto(orderItem);
    }

    public void changeOrderStatus(Long orderId, UpdateStatusOrderDto statusOrderDto) {
        log.info("Changing status of order: {}", orderId);

        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order didn't find"));

        order.setStatus(statusOrderDto.getStatus());
        orderRepository.save(order);

        log.info("Changed status of order: {}", orderId);
    }

    @Transactional
    @Override
    public void deleteOrderItem(Long orderItemId) {
        log.info("Deleting order item: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findByIdAndDeletedFalse(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("order item doesn't find"));

        orderItem.setDeleted(true);
        orderItemRepository.save(orderItem);

        log.info("Order item deleted successfully: {}", orderItem.getId());
    }


    @Transactional
    @Override
    public OrderDto placeOrder(Long orderId) {
        log.info("Placing order: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setStatus(Status.PLACED);
        OrderDto orderDto = orderMapper.toOrderDto(order);
        BigDecimal orderItemTotalCost = new BigDecimal("0.00");
        for (OrderItemDto dto: orderDto.getItems()) {
            if(!dto.isDeleted()){
                orderItemTotalCost = orderItemTotalCost.add(dto.getPrice()
                        .multiply(BigDecimal.valueOf(dto.getQuantity())));
            }
        }
        order.setTotalCost(getTotalCostForNewOrder(order));
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send(ORDER_TOPIC, String.format("%s;%s;%s", savedOrder.getId(),
                savedOrder.getTotalCost(), savedOrder.getPaymentMethod()));
        log.info("Order placed successfully: {}", savedOrder.getId());
        return orderMapper.toOrderDto(savedOrder);
    }


    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        log.info("Deleting order: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order doesn't find"));
        order.setDeleted(true);
        for (OrderItem orderItem: order.getOrderItems()) {
            orderItem.setDeleted(true);
        }
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
        log.info("Order deleted successfully: {}", order.getId());
    }
}
