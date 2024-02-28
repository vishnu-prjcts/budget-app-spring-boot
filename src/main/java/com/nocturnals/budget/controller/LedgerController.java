package com.nocturnals.budget.controller;

import com.nocturnals.budget.db.dao.LedgerService;
import com.nocturnals.budget.db.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/transaction")
public class LedgerController {
    private final LedgerService ledgerService;

    @Autowired
    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<Object> saveTransaction(@RequestBody Transaction transaction) {
        try {
            return ResponseEntity.ok(ledgerService.save(transaction));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Transaction description must be unique");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getTransaction(
            @RequestParam(value = "transaction-id", required = false) Long id,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "start-date", required = false) Date startDate,
            @RequestParam(value = "end-date", required = false) Date endDate,
            @RequestParam(value = "is-expense", required = false) Boolean isExpense,
            @RequestParam(value = "is-recurring", required = false) Boolean isRecurring,
            @RequestParam(value = "recurring-date", required = false) Integer recurringDate,
            @RequestParam(value = "account-id", required = false) Long accountId,
            @RequestParam(value = "category-id", required = false) Long categoryId,
            @RequestParam(value = "start-amount", required = false) BigDecimal startingAmount,
            @RequestParam(value = "end-amount", required = false) BigDecimal endingAmount,
            @RequestParam(value = "amount-comparison", required = false) String amountComparison) {
        List<Transaction> transactions;
        if(id != null) {
            Transaction transaction = ledgerService.findById(id);
            if(transaction == null) {
                return ResponseEntity.notFound().build();
            }
            transactions = Collections.singletonList(transaction);
        } else if(description != null) {
            transactions = ledgerService.findByDescription(description, startDate, endDate);
        } else if(accountId != null || categoryId != null) {
            transactions = ledgerService.findByAccountAndCategory(accountId, categoryId, startDate, endDate);
        } else if(startDate != null) {
            transactions = ledgerService.findByDateRange(startDate, endDate);
        } else if(isExpense != null || isRecurring != null || recurringDate != null) {
            transactions = ledgerService.findByIsRecurringAndIsExpense(isRecurring, isExpense, recurringDate);
        } else if(startingAmount != null) {
            transactions = ledgerService.findByAmount(startingAmount, endingAmount,amountComparison);
        } else {
            transactions = ledgerService.findAll();
        }
        if(transactions == null || transactions.isEmpty()) {
            return ResponseEntity.badRequest().body("No transactions found");
        } else {
            return ResponseEntity.ok(transactions);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable Long id) {
        try {
            ledgerService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
