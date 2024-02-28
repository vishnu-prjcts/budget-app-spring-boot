package com.nocturnals.budget.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "account_type")
@EntityListeners(AuditingEntityListener.class)
@Data
public class AccountType {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String type;
    @CreatedDate
    @Column(name = "created_at")
    Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    Timestamp updatedAt;
    @OneToMany(mappedBy = "accountType")
    private List<Account> accounts;
}
