package com.nocturnals.budget.db.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bank")
@EntityListeners(AuditingEntityListener.class)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bank {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    @CreatedDate
    @Column(name = "created_at")
    Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    Timestamp updatedAt;
    @OneToMany(mappedBy = "bank")
    private List<Account> accounts;
}
