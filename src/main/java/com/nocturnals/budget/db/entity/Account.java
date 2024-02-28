package com.nocturnals.budget.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    @Builder.Default
    BigDecimal balance = BigDecimal.valueOf(0.0);
    @Column(name = "is_loan_account")
    Boolean isLoanAccount;
    @Column(name = "total_amount")
    BigDecimal totalAmount;
    @Column(name = "interest_rate")
    Double interestRate;
    @Column(name = "start_date")
    Date startDate;
    @Column(name = "end_date")
    Date endDate;
    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "id", insertable = false, updatable = false)
    Bank bank;
    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    AccountType accountType;
    @CreatedDate
    @Column(name = "created_at")
    Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    Timestamp updatedAt;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
}
