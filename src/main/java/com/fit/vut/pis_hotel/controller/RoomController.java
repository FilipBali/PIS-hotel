package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/room")
public class RoomController {

    RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDO> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping(value = "/{id}")
    public RoomDO getRoom(@PathVariable("id") Long id) {
        return roomService.getRoom(id);
    }

    @GetMapping(value = "/available-rooms")
    public List<RoomDO> getAvailableRooms(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                          @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return roomService.getAvailableRooms(from, to);
    }

    @PostMapping
    public void createRoom(@RequestBody RoomDO room) {
        roomService.createRoom(room);
    }

    @DeleteMapping(value = "{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    @PutMapping(path = {"{id}"})
    public void updateRoom(
            @PathVariable("id") Long id, @RequestBody RoomDO room) {
        roomService.updateRoom(id, room);
    }
}
