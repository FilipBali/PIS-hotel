package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stay")
public class StayDO {

    @Id
    @SequenceGenerator(
            name = "stay_sequence",
            sequenceName = "stay_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stay_sequence"
    )
    private Long id;

    @Column(
            name = "accommodated_number",
            nullable = false
    )
    private Integer accommodatedNumber;

    @Column(
            name = "date_from",
            nullable = false
    )
    private LocalDate dateFrom;

    @Column(
            name = "date_to",
            nullable = false
    )
    private LocalDate dateTo;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "state",
            nullable = false
    )
    private StateEnum state;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "board_type",
            nullable = false
    )
    private BoardTypeEnum boardType;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "FK_StayCreator", referencedColumnName = "id")
    private HostDO stayCreator;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_type",
            nullable = false
    )
    private PaymentTypeEnum paymentType;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "stay_room",
            joinColumns = @JoinColumn(name = "stay_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<RoomDO> rooms;

    public StayDO(Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StateEnum state, BoardTypeEnum boardType, HostDO stayCreator, PaymentTypeEnum paymentType, List<RoomDO> rooms) {
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.stayCreator = stayCreator;
        this.paymentType = paymentType;
        this.rooms = rooms;
    }

    public StayDTO toDto() {
        return new StayDTO(
                this.getId(),
                this.getAccommodatedNumber(),
                this.getDateFrom(),
                this.getDateTo(),
                this.getState(),
                this.getBoardType(),
                this.getStayCreator().getId(),
                this.getPaymentType(),
                this.getRooms().stream().map(RoomDO::getId).collect(Collectors.toList()));
    }
}
