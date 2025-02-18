package ru.aston.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recommended_product_id", nullable = false)
    private Long productId;

    @Column(name = "rating", nullable = false)
    private Integer  rating;

    @Column(name = "score")
    private Double score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recommendation)) return false;
        Recommendation recommendation = (Recommendation) o;
        return Objects.equals(id, recommendation.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}