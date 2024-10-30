package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Role;
import org.example.footballanalyzer.Data.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    List<Role> findAllByRoleNameNot(RoleName roleName);
}
