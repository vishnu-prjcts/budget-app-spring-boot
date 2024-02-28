package com.nocturnals.budget.db.repository;

import com.nocturnals.budget.db.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    AccountType findByType(String name);
}
