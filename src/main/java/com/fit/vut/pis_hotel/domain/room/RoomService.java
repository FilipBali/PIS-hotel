package com.fit.vut.pis_hotel.domain.room;

import com.fit.vut.pis_hotel.domain.room.enums.RoomStateEnum;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryRepository;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final StayService stayService;

    public List<RoomDO> getRooms() {
        return roomRepository.findAll();
    }

    public RoomDO getRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room with id: " + id + " does not exist."));
    }

    public void createRoom(RoomDO room) {roomRepository.save(room); }

    public void deleteRoom(Long id) {
        boolean room_exists = roomRepository.existsById(id);
        if (room_exists) {
            log.info("Deleting room with id: " + id);
            roomRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Room with id: " + id + " does not exist.");
        }
    }

    @Transactional
    public void updateRoom(Long id, RoomDO roomBody) {

        RoomDO room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room with id: " + id + " does not exist."));

        Integer roomNumber = roomBody.getRoomNumber();
        if (isIntegerValid(roomNumber, room.getRoomNumber())) {
            room.setRoomNumber(roomNumber);
        }

        RoomStateEnum state = roomBody.getState();
        if (state != null && state != room.getState()) {
            room.setState(state);
        }

        Integer bedsNum = roomBody.getBedsNum();
        if (isIntegerValid(bedsNum, room.getBedsNum())) {
            room.setBedsNum(bedsNum);
        }

        RoomCategoryDO roomCategory = roomBody.getRoomCategory();
        if (roomCategory != null) {
            boolean cat_exists = roomCategoryRepository.existsById(roomCategory.getId());
            if (cat_exists) {
                room.setRoomCategory(roomCategory);
            }
        }

    }

    private boolean isIntegerValid(Integer integerToValidate, Integer originalInteger) {
        return integerToValidate != null && integerToValidate > 0 && !Objects.equals(integerToValidate, originalInteger);
    }

    public List<RoomDO> getAvailableRooms(LocalDate from, LocalDate to) {
        LocalDateTime dateFrom = from.atTime(11, 0);
        LocalDateTime dateTo = to.atTime(14, 0);
        List<RoomDO> rooms = getRooms();
        List<RoomDO> availableRooms = new ArrayList<>(List.of());
        for (RoomDO room : rooms) {
            boolean isRoomReserved = room.getStays().stream().anyMatch(stayDO ->
                    isDateInBetween(dateFrom, dateTo, stayDO.getDateFrom().atTime(11, 0)) ||
                            isDateInBetween(dateFrom, dateTo, stayDO.getDateFrom().atTime(14, 0)));
            if (!isRoomReserved) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    private boolean isDateInBetween(final LocalDateTime min, final LocalDateTime max, final LocalDateTime date) {
        return (date.isBefore(max) && date.isAfter(min)) || date.isEqual(min) || date.isEqual(max);
    }
}
