package com.fit.vut.pis_hotel.domain.stay.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceDO, Long> {

    @Query("SELECT s FROM ServiceDO s WHERE s.stay.id = ?1")
    List<ServiceDO> findServicesByStayId(Long id);

}
