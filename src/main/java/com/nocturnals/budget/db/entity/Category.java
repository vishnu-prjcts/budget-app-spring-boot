package com.nocturnals.budget.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    BigDecimal budget;
    @Column(name = "remaining_budget")
    BigDecimal remainingBudget;
    @Column(name = "is_rolling_budget")
    Boolean isRollingBudget;
    @CreatedDate
    @Column(name = "created_at")
    Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    Timestamp updatedAt;
    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
}
