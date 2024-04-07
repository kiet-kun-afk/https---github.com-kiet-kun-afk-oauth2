package com.j6d7.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String photo;
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Authority> authorities;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "roleid")
    private Role role;
}
