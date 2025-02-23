package ru.petproject.ecommerce.order_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    @Column(name = "status")
    @NotNull
    private Status status = Status.NEW;

    @Column(name = "total_Cost", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal totalCost;

    @Column(name = "payment_method", columnDefinition = "VARCHAR(20) DEFAULT 'SBPFake'")
    private String paymentMethod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "deleted", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted;

    public enum Status {
        NEW,
        AWAITING_PAYMENT,
        PAID_SUCCESS,
        PAID_FAILURE,
        CANCELLED,
        DELIVERED
    }

    @PrePersist
    protected void onCreate() {
        status = Status.NEW;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


