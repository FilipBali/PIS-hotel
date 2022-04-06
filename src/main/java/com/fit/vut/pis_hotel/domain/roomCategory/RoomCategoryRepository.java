package com.fit.vut.pis_hotel.domain.roomCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategoryDO, Long> {

}
