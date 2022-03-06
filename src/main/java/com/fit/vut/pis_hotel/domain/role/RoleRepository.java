package com.fit.vut.pis_hotel.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleDO, Long> {
    RoleDO findByName(String name);
}
