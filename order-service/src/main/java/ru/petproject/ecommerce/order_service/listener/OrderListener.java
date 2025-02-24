package ru.petproject.ecommerce.order_service.listener;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.petproject.ecommerce.order_service.dto.UpdateStatusOrderDto;
import ru.petproject.ecommerce.order_service.exception.WrongNumberArgException;
import ru.petproject.ecommerce.order_service.model.Status;
import ru.petproject.ecommerce.order_service.service.OrderServiceImpl;

@Slf4j
@Component
@ToString
@RequiredArgsConstructor
public class OrderListener {
    private final OrderServiceImpl orderService;
    public static final String PAYMENT_TOPIC = "order-topic";
    public static final String PAYMENT_SUCCESS = "true";

    @KafkaListener(topics = PAYMENT_TOPIC)
    public void orderListen(String payload) {
        String[] payloadParts = payload.split(";");
        Long orderId = Long.parseLong(payloadParts[0]);
        if (payloadParts.length != 2) {
            throw new WrongNumberArgException("Wrong number of arguments: " + payloadParts.length);
        }
        else {
            UpdateStatusOrderDto updateStatusOrderDto;
            if (payloadParts[1].equals(PAYMENT_SUCCESS)) {
                updateStatusOrderDto = UpdateStatusOrderDto.builder()
                        .status(Status.PAID_SUCCESS)
                        .build();
                log.info("Payment for order {} was successful", orderId);
            }
            else {
                updateStatusOrderDto = UpdateStatusOrderDto.builder()
                        .status(Status.PAID_FAILURE)
                        .build();
                log.info("Payment for order {} failed", orderId);
            }
            orderService.changeOrderStatus(orderId, updateStatusOrderDto);
        }
    }
}
