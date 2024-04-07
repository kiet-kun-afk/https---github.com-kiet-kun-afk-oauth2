package com.j6d7.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j6d7.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);
}
