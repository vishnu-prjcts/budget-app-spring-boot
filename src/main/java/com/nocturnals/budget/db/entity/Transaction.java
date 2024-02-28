package com.nocturnals.budget.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "ledger")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    String description;
    BigDecimal amount;
    @Column(name = "transaction_date")
    Date transactionDate;
    @Column(name = "is_expense")
    Boolean isExpense;
    @Column(name = "is_recurring")
    Boolean isRecurring;
    @Column(name = "recurring_date")
    Integer recurringDate;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    Account account;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    Category category;
    @CreatedDate
    @Column(name = "created_at")
    Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    Timestamp updatedAt;
}
