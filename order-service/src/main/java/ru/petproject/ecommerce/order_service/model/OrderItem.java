package ru.petproject.ecommerce.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    @NotNull
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    @NotNull
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "deleted")
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        deleted = false;
    }
}


