package com.fit.vut.pis_hotel.domain.room;

import com.fit.vut.pis_hotel.domain.room.enums.RoomStateEnum;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private RoomCategoryRepository roomCategoryRepository;

    public List<RoomDO> getRooms() {
        return roomRepository.findAll();
    }

    public RoomDO getRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room with id: " + id + "does not exist."));
    }

    public void createRoom(RoomDO room) {roomRepository.save(room); }

    public void deleteRoom(Long id) {
        boolean room_exists = roomRepository.existsById(id);
        if (room_exists) {
            roomRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Room with id: " + id + " does not exist.");
        }
    }

    @Transactional
    public void updateRoom(Long id, Integer roomNumber, RoomStateEnum state, Integer bedsNum, RoomCategoryDO roomCategory) {

        RoomDO room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Room with id: " + id + "does not exist."));

        if (isIntegerValid(roomNumber, room.getRoomNumber())) {
            room.setRoomNumber(roomNumber);
        }

        if (state != null && state != room.getState()) {
            room.setState(state);
        }

        if (isIntegerValid(bedsNum, room.getBedsNum())) {
            room.setBedsNum(bedsNum);
        }

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
}
