package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByType(String type);
}
