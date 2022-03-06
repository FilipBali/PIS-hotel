package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import com.fit.vut.pis_hotel.domain.user.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private StayStateEnum state;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "board_type",
            nullable = false
    )
    private BoardTypeEnum boardType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_StayCreator", referencedColumnName = "id")
    private UserDO stayCreator;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_type",
            nullable = false
    )
    private PaymentTypeEnum paymentType;

    public StayDO(Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StayStateEnum state, BoardTypeEnum boardType, UserDO stayCreator, PaymentTypeEnum paymentType) {
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.stayCreator = stayCreator;
        this.paymentType = paymentType;
    }
}
