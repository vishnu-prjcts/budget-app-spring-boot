package com.nocturnals.budget.db.dao;

import com.nocturnals.budget.db.entity.Bank;
import com.nocturnals.budget.db.repository.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService{
    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    public Bank findById(Long id) {
        return bankRepository.findById(id).orElse(null);
    }

    public Bank findByName(String name) {
        return bankRepository.findByNameIgnoreCase(name).orElse(null);
    }

    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    public void deleteById(Long id) {
        bankRepository.deleteById(id);
    }
}