package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryService;
import com.fit.vut.pis_hotel.domain.roomCategory.enums.RoomTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/room_category")
public class RoomCategoryController {

    RoomCategoryService roomCategoryService;

    @Autowired
    public RoomCategoryController(RoomCategoryService roomCategoryService) {this.roomCategoryService = roomCategoryService; }

    @GetMapping
    public List<RoomCategoryDO> getRoomCategories() {return roomCategoryService.getRoomCategories(); }

    @GetMapping(value = "/{id}")
    public RoomCategoryDO getRoomCategory(@PathVariable("id") Long id) {return roomCategoryService.getRoomCategory(id); }

    @PostMapping
    public void createRoomCategory(@RequestBody RoomCategoryDO category) {roomCategoryService.createRoomCategory(category); }

    @DeleteMapping(value = "{id}")
    public void deleteRoomCategory(@PathVariable Long id) {roomCategoryService.deleteRoomCategory(id); }

    @PutMapping(path = {"{id}"})
    public void updateRoomCategory(
            @PathVariable("id") Long id,
            @RequestParam(required = false) RoomTypeEnum type,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) Double cost_per_bed) {
        roomCategoryService.updateRoomCategory(id, type, equipment, cost_per_bed);
    }
}
