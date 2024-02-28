package com.nocturnals.budget.controller;

import com.nocturnals.budget.db.dao.AccountService;
import com.nocturnals.budget.db.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Account account) {
        try {
            return ResponseEntity.ok(accountService.save(account));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Account name must be unique");
        }
    }

    @GetMapping
    public ResponseEntity<Object> findByIdOrName(@RequestParam(value = "account-id", required = false) Long id,
                                  @RequestParam(value = "account-name", required = false) String name) {
        Account account = null;
        if(id != null) {
            account = accountService.findById(id);
            if(account != null && name != null) {
                if(!name.equalsIgnoreCase(account.getName())) {
                    return ResponseEntity.badRequest().body("Account name and id does not match");
                }
            }
        } else if (name != null) {
            account = accountService.findByName(name);
        } else {
            return ResponseEntity.badRequest().body("Account id or name must be provided");
        }
        if(account == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(account);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            accountService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
