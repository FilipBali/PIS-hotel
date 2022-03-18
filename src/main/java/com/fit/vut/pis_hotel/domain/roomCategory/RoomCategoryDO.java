package com.fit.vut.pis_hotel.domain.roomCategory;

import com.fit.vut.pis_hotel.domain.roomCategory.enums.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roomCategory")
public class RoomCategoryDO {

    @Id
    @SequenceGenerator(
            name = "room_cat_sequence",
            sequenceName = "room_cat_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_cat_sequence"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false
    )
    private RoomTypeEnum type;

    @Column(
            name = "equipment",
            nullable = false
    )
    private String equipment;

    @Column(
            name = "cost_per_bed",
            nullable = false
    )
    private Double cost_per_bed;

    public RoomCategoryDO(RoomTypeEnum type, String equipment, Double cost_per_bed) {
        this.type = type;
        this.equipment = equipment;
        this.cost_per_bed = cost_per_bed;
    }
}
