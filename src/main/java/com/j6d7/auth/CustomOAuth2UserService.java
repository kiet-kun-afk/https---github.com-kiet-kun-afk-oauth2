package com.j6d7.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.j6d7.entity.Account;
import com.j6d7.entity.Role;
import com.j6d7.repo.AccountRepository;
import com.j6d7.repo.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String typeOAuth = userRequest.getClientRegistration().getClientName();
        OAuth2UserInfo oAuth2UserInfo = switch (typeOAuth) {
            case "Google" -> new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
            default -> new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        };
        Account existingAccount = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        Account account;
        if (existingAccount != null) {
            account = updateExistingAccount(existingAccount, oAuth2UserInfo, typeOAuth);
        } else {
            account = registerNewUser(userRequest, oAuth2UserInfo, typeOAuth);
        }
        return UserRoot.create(account, oAuth2UserInfo.getAttributes());
    }

    private Role createRole(String roleStr) {
        Role role = roleRepository.findByName(roleStr);
        if (role == null) {
            role = new Role(roleStr, roleStr);
            role = roleRepository.save(role);
        }
        return role;
    }

    private Account updateExistingAccount(Account existingAccount, OAuth2UserInfo oAuth2UserInfo, String typeRequest) {
        existingAccount.setUsername(oAuth2UserInfo.getName());
        existingAccount.setRole(typeRequest.equalsIgnoreCase("google") ? createRole("google") : createRole("github"));
        existingAccount.setPhoto(oAuth2UserInfo.getImageUrl());
        return accountRepository.save(existingAccount);
    }

    private Account registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo,
            String typeRequest) {
        Account account = new Account();
        account.setRole(typeRequest.equalsIgnoreCase("google") ? createRole("google") : createRole("github"));
        account.setUsername(oAuth2UserInfo.getName());
        account.setEmail(oAuth2UserInfo.getEmail());
        account.setPhoto(oAuth2UserInfo.getImageUrl());
        return accountRepository.save(account);
    }
}