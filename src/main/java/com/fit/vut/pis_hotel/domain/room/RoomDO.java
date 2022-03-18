package com.fit.vut.pis_hotel.domain.room;

import com.fit.vut.pis_hotel.domain.room.enums.RoomStateEnum;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class RoomDO {

    @Id
    @SequenceGenerator(
            name = "room_sequence",
            sequenceName = "room_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_sequence"
    )
    private Long id;

    @Column(
            name = "room_number",
            nullable = false
    )
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "state",
            nullable = false
    )
    private RoomStateEnum state;

    @Column(
            name = "beds_num",
            nullable = false
    )
    private Integer bedsNum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_RoomCategory", referencedColumnName = "id")
    private RoomCategoryDO roomCategory;

    public RoomDO(Integer roomNumber, Integer bedsNum, RoomCategoryDO roomCategory) {
        this.roomNumber = roomNumber;
        this.state = RoomStateEnum.AVAILABLE;
        this.bedsNum = bedsNum;
        this.roomCategory = roomCategory;
    }
}
