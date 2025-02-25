package ru.petproject.ecommerce.productService.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class UserListener {
    private final Map<String, Boolean> authorizationCache = new ConcurrentHashMap<>();
    private final Map<String, Boolean> adminCache = new ConcurrentHashMap<>();

    @KafkaListener(topics = "user-registration", groupId = "user")
    public void consume(String message) {
        // Разделяем сообщение на части
        String[] parts = message.split(",");
        if (parts.length == 3) {
            String userLogin = parts[0];
            boolean isAuthorized = Boolean.parseBoolean(parts[1]);
            boolean isAdmin = Boolean.parseBoolean(parts[2]);

            // Сохраняем статус пользователя в кэш
            authorizationCache.put(userLogin, isAuthorized);
            adminCache.put(userLogin, isAdmin);
        }
    }
    public boolean isUserAuthorized(String userLogin) {
        return authorizationCache.getOrDefault(userLogin, false);
    }

    public boolean isUserAdmin(String userLogin) {
        return adminCache.getOrDefault(userLogin, false);
    }

}