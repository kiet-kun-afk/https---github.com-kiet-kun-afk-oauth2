package com.j6d7.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.j6d7.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
}
