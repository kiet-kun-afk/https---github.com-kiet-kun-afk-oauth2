package com.j6d7.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.j6d7.entity.Authority;
import com.j6d7.repo.AccountRepository;
import com.j6d7.repo.AuthorityRepository;
import com.j6d7.repo.RoleRepository;

@RestController
public class AuthorityRestController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/rest/authorities")
    public Map<String, Object> getAuthorities() {
        Map<String, Object> data = new HashMap<>();
        data.put("authorities", authorityRepository.findAll());
        data.put("accounts", accountRepository.findAll());
        data.put("roles", roleRepository.findAll());
        return data;
    }

    @SuppressWarnings("null")
    @DeleteMapping("/rest/authorities/{id}")
    public void delete(@PathVariable Integer id) {
        authorityRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    @PostMapping("/rest/authorities")
    public Authority post(@RequestBody Authority authority) {
        return authorityRepository.save(authority);
    }
}
