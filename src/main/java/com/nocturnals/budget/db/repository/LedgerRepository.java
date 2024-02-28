package com.nocturnals.budget.db.repository;

import com.nocturnals.budget.db.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDescriptionContainingIgnoreCase(String description);

    List<Transaction> findByTransactionDate(Date date);

    List<Transaction> findByTransactionDateBetween(Date startDate, Date endDate);

    List<Transaction> findByAmount(BigDecimal amount);

    List<Transaction> findByAmountGreaterThanOrEqual(BigDecimal amount);

    List<Transaction> findByAmountLessThanOrEqual(BigDecimal amount);

    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Transaction> findByIsRecurring(Boolean isRecurring);

    List<Transaction> findByIsExpense(Boolean isExpense);

    List<Transaction> findByRecurringDate(Integer recurringDate);

    List<Transaction> findByRecurringDateGreaterThanOrEqual(Integer recurringDate);

    List<Transaction> findByRecurringDateLessThanOrEqual(Integer recurringDate);

    List<Transaction> findByRecurringDateBetween(Integer minRecurringDate, Integer maxRecurringDate);

    List<Transaction> findByIsRecurringAndIsExpense(Boolean isRecurring, Boolean isExpense);

    List<Transaction> findByIsExpenseAndRecurringDate(Integer recurringDate, Boolean isExpense);

    List<Transaction> findByIsExpenseAndRecurringDateBetween(
            Integer minRecurringDate, Integer maxRecurringDate, Boolean isExpense);

    List<Transaction> findByAccount_Id(Long id);

    List<Transaction> findByCategory_Id(Long id);

    List<Transaction> findByAccount_IdAndCategory_Id(Long accountId, Long categoryId);

    List<Transaction> findByDescriptionContainingIgnoreCaseAndTransactionDateBetween(String description, Date startDate, Date endDate);

    List<Transaction> findByDescriptionContainingIgnoreCaseAndTransactionDate(String description, Date date);

    List<Transaction> findByAccount_IdAndTransactionDate(Long accountId, Date date);

    List<Transaction> findByCategory_IdAndTransactionDate(Long categoryId, Date date);

    List<Transaction> findByAccount_IdAndCategory_IdAndTransactionDate(Long accountId, Long categoryId, Date date);

    List<Transaction> findByAccount_IdAndTransactionDateBetween(Long accountId, Date startDate, Date endDate);

    List<Transaction> findByCategory_IdAndTransactionDateBetween(Long categoryId, Date startDate, Date endDate);

    List<Transaction> findByAccount_IdAndCategory_IdAndTransactionDateBetween(
            Long accountId, Long categoryId, Date startDate, Date endDate);
}
