package com.nocturnals.budget.db.dao;

import com.nocturnals.budget.db.entity.AccountType;
import com.nocturnals.budget.db.repository.AccountTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    public AccountType findByType(String type) {
        return accountTypeRepository.findByType(type);
    }
    public AccountType save(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    public AccountType findById(Long id) {
        return accountTypeRepository.findById(id).orElse(null);
    }

    public Iterable<AccountType> findAll() {
        return accountTypeRepository.findAll();
    }

    public void deleteById(Long id) {
        accountTypeRepository.deleteById(id);
    }

}
