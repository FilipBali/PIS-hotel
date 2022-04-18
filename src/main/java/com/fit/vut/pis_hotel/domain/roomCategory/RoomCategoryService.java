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
    public void updateRoomCategory(Long id, RoomCategoryDO roomCategoryBody) {
        RoomCategoryDO category = roomCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room category with id: " + id + "does not exist."));

        RoomTypeEnum type = roomCategoryBody.getType();
        if (type != null && type != category.getType()) {
            category.setType(type);
        }

        String equipment = roomCategoryBody.getEquipment();
        if (equipment != null && !equipment.equals(category.getEquipment())) {
            category.setEquipment(equipment);
        }

        Double bedCost = roomCategoryBody.getCostPerBed();
        if (isFloatValid(bedCost, category.getCostPerBed())) {
            category.setCostPerBed(bedCost);
        }

    }

    private boolean isFloatValid(Double doubleToValidate, Double originalDouble) {
        return doubleToValidate != null && doubleToValidate >= 0.0 && !Objects.equals(doubleToValidate, originalDouble);
    }
}
