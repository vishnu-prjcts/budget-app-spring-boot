package com.nocturnals.budget.db.repository;

import com.nocturnals.budget.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByNameIgnoreCase(String name);
}
