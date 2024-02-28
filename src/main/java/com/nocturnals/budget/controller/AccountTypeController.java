package com.nocturnals.budget.controller;

import com.nocturnals.budget.db.dao.AccountTypeService;
import com.nocturnals.budget.db.entity.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account-type")
public class AccountTypeController {
    AccountTypeService accountTypeService;

    @Autowired
    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody AccountType accountType) {
        try {
            return ResponseEntity.ok(accountTypeService.save(accountType));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Account type must be unique");
        }
    }

    @GetMapping
    public ResponseEntity<Object> findAccountType(@RequestParam(value = "account-type-id", required = false) Long id,
                                                  @RequestParam(value = "account-type", required = false) String type) {
        AccountType accountType;
        if(id != null) {
            accountType = accountTypeService.findById(id);
            if(accountType != null && type != null) {
                if(!type.equalsIgnoreCase(accountType.getType())) {
                    return ResponseEntity.badRequest().body("Account type and id does not match");
                }
            }
        } else if (type != null) {
            accountType = accountTypeService.findByType(type);
        } else {
            return ResponseEntity.badRequest().body("Account id or type must be provided");
        }
        if(accountType == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(accountType);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(accountTypeService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            accountTypeService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
