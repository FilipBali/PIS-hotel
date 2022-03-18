package com.fit.vut.pis_hotel.domain.roomCategory;

import com.fit.vut.pis_hotel.domain.roomCategory.enums.RoomTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;

    public List<RoomCategoryDO> getRoomCategories() { return roomCategoryRepository.findAll(); }

    public RoomCategoryDO getRoomCategory(Long id) {
        return roomCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room category with id: " + id + "does not exist."));
    }

    public void createRoomCategory(RoomCategoryDO category) {roomCategoryRepository.save(category); }

    public void deleteRoomCategory(Long id) {
        boolean cat_exists = roomCategoryRepository.existsById(id);

        if (cat_exists) {
            roomCategoryRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Room category with id: " + id + " does not exist.");
        }
    }

    @Transactional
    public void updateRoomCategory(Long id, RoomTypeEnum type, String equipment, Double cost_per_bed){
        RoomCategoryDO category = roomCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room category with id: " + id + "does not exist."));

        if (type != null && type != category.getType()) {
            category.setType(type);
        }

        if (equipment != null && !equipment.equals(category.getEquipment())) {
            category.setEquipment(equipment);
        }

        if (isFloatValid(cost_per_bed, category.getCost_per_bed())) {
            category.setCost_per_bed(cost_per_bed);
        }

    }

    private boolean isFloatValid(Double doubleToValidate, Double originalDouble) {
        return doubleToValidate != null && doubleToValidate >= 0.0 && !Objects.equals(doubleToValidate, originalDouble);
    }
}
