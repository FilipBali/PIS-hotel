package com.fit.vut.pis_hotel.domain.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<HostDO, Long> {

    @Query("SELECT u FROM HostDO u WHERE u.email = ?1")
    Optional<HostDO> findHostByEmail(String email);

    @Query("SELECT u FROM HostDO u WHERE u.firstName = ?1 AND u.lastName = ?2")
    Optional<HostDO> findHostByName(String firstName, String lastName);

}
