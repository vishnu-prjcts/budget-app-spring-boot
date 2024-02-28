package com.nocturnals.budget.db.dao;

import com.nocturnals.budget.db.entity.Account;
import com.nocturnals.budget.db.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account findByName(String name) {
        return accountRepository.findByNameIgnoreCase(name);
    }

    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

}
