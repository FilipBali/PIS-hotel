package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.room.RoomService;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import com.fit.vut.pis_hotel.domain.room.enums.RoomStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/room")
public class RoomController {

    RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) { this.roomService = roomService; }

    @GetMapping
    public List<RoomDO> getRooms() {return roomService.getRooms(); }

    @GetMapping(value = "/{id}")
    public RoomDO getRoom(@PathVariable("id") Long id) { return roomService.getRoom(id); }

    @PostMapping
    public void createRoom(@RequestBody RoomDO room) {roomService.createRoom(room); }

    @DeleteMapping(value = "{id}")
    public void deleteRoom(@PathVariable Long id) {roomService.deleteRoom(id); }

    @PutMapping(path = {"{id}"})
    public void updateRoom(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Integer roomNumber,
            @RequestParam(required = false) RoomStateEnum state,
            @RequestParam(required = false) Integer bedsNum,
            @RequestParam(required = false) RoomCategoryDO roomCategory) {
        roomService.updateRoom(id, roomNumber, state, bedsNum, roomCategory);
    }
}
