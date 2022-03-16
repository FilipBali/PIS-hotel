package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.room.RoomService;
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
            @PathVariable("id") Integer roomNumber,
            @PathVariable("id") RoomStateEnum state,
            @PathVariable("id") Integer bedsNum) {
        roomService.updateRoom(id, roomNumber, state, bedsNum);
    }
}
