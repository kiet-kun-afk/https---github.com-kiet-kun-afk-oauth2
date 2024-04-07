package com.j6d7.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.j6d7.entity.Account;
import com.j6d7.repo.AccountRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @SuppressWarnings("null")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // --
        Account account = accountRepository.findById(username).get();
        if (account == null) {
            throw new UsernameNotFoundException("Can not found with username: " +
                    username);
        }
        return UserRoot.create(account);
        // --
        // Account account = accountRepository.findById(username).get();
        // if (account == null) {
        // throw new UsernameNotFoundException("Can not found with username: " +
        // username);
        // }
        // String password = account.getPassword();
        // String[] roles = account.getAuthorities().stream().map(au ->
        // au.getRole().getId())
        // .collect(Collectors.toList()).toArray(new String[0]);
        // return User.withUsername(username).password(password).roles(roles).build();
        // --
    }
}
