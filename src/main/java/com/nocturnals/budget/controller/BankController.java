package com.nocturnals.budget.controller;

import com.nocturnals.budget.db.dao.BankService;
import com.nocturnals.budget.db.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/bank")
public class BankController {
    BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Bank bank) {
        try {
            return ResponseEntity.ok(bankService.save(bank));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Bank name must be unique");
        }
    }

    @GetMapping
    public ResponseEntity<Object> findBankByIdOrName(@RequestParam(value = "bank-id", required = false) Long id,
                                           @RequestParam(value = "bank-name", required = false) String name) {
        Bank bank;
        if(id != null) {
            bank = bankService.findById(id);
            if(bank != null && name != null) {
                if(!name.equalsIgnoreCase(bank.getName())) {
                    return ResponseEntity.badRequest().body("Bank name and id does not match");
                }
            }
        } else if (name != null) {
            bank = bankService.findByName(name);
        } else {
            return ResponseEntity.badRequest().body("Bank id or name must be provided");
        }
        if(bank == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bank);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(bankService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            bankService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
