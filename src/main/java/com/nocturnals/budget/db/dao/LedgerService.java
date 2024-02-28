package com.nocturnals.budget.db.dao;

import com.nocturnals.budget.db.entity.Transaction;
import com.nocturnals.budget.db.repository.LedgerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class LedgerService {
    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public Transaction save(Transaction transaction) {
        return ledgerRepository.save(transaction);
    }

    public Transaction findById(Long id) {
        return ledgerRepository.findById(id).orElse(null);
    }

    public List<Transaction> findTransactionsByDate(Date date) {
        return ledgerRepository.findByTransactionDate(date);
    }
    public List<Transaction> findTransactionsByDescription(String description) {
        return ledgerRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public List<Transaction> findTransactionsByDateRange(Date startDate, Date endDate) {
        return ledgerRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<Transaction> findAll() {
        return ledgerRepository.findAll();
    }

    public void deleteById(Long id) {
        ledgerRepository.deleteById(id);
    }

    public List<Transaction> findByDescription(String description, Date startDate, Date endDate) {
        if(startDate != null) {
            if(endDate != null) {
                return ledgerRepository.findByDescriptionContainingIgnoreCaseAndTransactionDateBetween(description,
                        startDate, endDate);
            } else {
                return ledgerRepository.findByDescriptionContainingIgnoreCaseAndTransactionDate(description, startDate);
            }
        } else {
            return ledgerRepository.findByDescriptionContainingIgnoreCase(description);
        }
    }

    public List<Transaction> findByAccountAndCategory(Long accountId, Long categoryId, Date startDate, Date endDate) {
        if(accountId != null) {
            if (categoryId != null) {
                if(startDate != null) {
                    if(endDate != null) {
                        return ledgerRepository.findByAccount_IdAndCategory_IdAndTransactionDateBetween(accountId,
                                categoryId, startDate, endDate);
                    } else {
                        return ledgerRepository.findByAccount_IdAndCategory_IdAndTransactionDate(accountId, categoryId,
                                startDate);
                    }
                } else {
                    return ledgerRepository.findByAccount_IdAndCategory_Id(accountId, categoryId);
                }
            } else {
                if(startDate != null) {
                    if(endDate != null) {
                        return ledgerRepository.findByAccount_IdAndTransactionDateBetween(accountId, startDate, endDate);
                    } else {
                        return ledgerRepository.findByAccount_IdAndTransactionDate(accountId, startDate);
                    }
                } else {
                    return ledgerRepository.findByAccount_Id(accountId);
                }
            }
        } else {
            if(startDate != null) {
                if(endDate != null) {
                    return ledgerRepository.findByCategory_IdAndTransactionDateBetween(categoryId, startDate, endDate);
                } else {
                    return ledgerRepository.findByCategory_IdAndTransactionDate(categoryId, startDate);
                }
            } else {
                return ledgerRepository.findByCategory_Id(categoryId);
            }
        }
    }

    public List<Transaction> findByDateRange(Date startDate, Date endDate) {
        if(endDate != null) {
            return ledgerRepository.findByTransactionDateBetween(startDate, endDate);
        } else {
            return ledgerRepository.findByTransactionDate(startDate);
        }
    }

    public List<Transaction> findByIsRecurringAndIsExpense(Boolean isRecurring, Boolean isExpense, Integer recurringDate) {
        if(isRecurring != null) {
            if(isExpense != null) {
                if(recurringDate != null) {
                    return ledgerRepository.findByIsExpenseAndRecurringDate(recurringDate, isExpense);
                } else {
                    return ledgerRepository.findByIsRecurringAndIsExpense(isRecurring, isExpense);
                }
            } else if (recurringDate != null) {
                return ledgerRepository.findByRecurringDate(recurringDate);
            } else {
                return ledgerRepository.findByIsRecurring(isRecurring);
            }
        } else if(isExpense != null) {
            if(recurringDate != null) {
                return ledgerRepository.findByIsExpenseAndRecurringDate(recurringDate, isExpense);
            } else {
                return ledgerRepository.findByIsExpense(isExpense);
            }
        } else if(recurringDate != null) {
            return ledgerRepository.findByRecurringDate(recurringDate);
        }
        return null;
    }

    public List<Transaction> findByAmount(BigDecimal startingAmount, BigDecimal endingAmount, String amountComparison) {
        if(startingAmount != null) {
            if(endingAmount != null) {
               return ledgerRepository.findByAmountBetween(startingAmount, endingAmount);
            } else if (amountComparison != null) {
                if(amountComparison.toLowerCase().contains("greater")) {
                    return ledgerRepository.findByAmountGreaterThanOrEqual(startingAmount);
                } else if (amountComparison.toLowerCase().contains("lesser")) {
                    return ledgerRepository.findByAmountLessThanOrEqual(startingAmount);
                } else if (amountComparison.toLowerCase().contains("equal")) {
                    return ledgerRepository.findByAmount(startingAmount);
                }
            } else {
                return ledgerRepository.findByAmount(startingAmount);
            }
        }
        return null;
    }
}
