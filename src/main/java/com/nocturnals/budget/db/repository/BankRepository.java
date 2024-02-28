package com.nocturnals.budget.db.repository;

import com.nocturnals.budget.db.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByNameIgnoreCase(String name);
}
