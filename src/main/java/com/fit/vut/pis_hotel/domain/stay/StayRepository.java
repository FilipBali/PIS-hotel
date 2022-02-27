package com.fit.vut.pis_hotel.domain.stay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StayRepository extends JpaRepository<StayDO, Long> {

}
