package com.fit.vut.pis_hotel.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long> {

    @Query("SELECT u FROM UserDO u WHERE u.email = ?1")
    Optional<UserDO> findUserByEmail(String email);


}
