package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StayDTO {

    private Long id;
    private Integer accommodatedNumber;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private StayStateEnum state;
    private BoardTypeEnum boardType;
    private Long stayCreatorId;
    private PaymentTypeEnum paymentType;
    private List<Long> roomIds;

    public StayDTO(Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StayStateEnum state, BoardTypeEnum boardType, Long stayCreatorId, PaymentTypeEnum paymentType, List<Long> roomIds) {
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.stayCreatorId = stayCreatorId;
        this.paymentType = paymentType;
        this.roomIds = roomIds;
    }

    public StayDTO(Long id, Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StayStateEnum state, BoardTypeEnum boardType, Long stayCreatorId, PaymentTypeEnum paymentType, List<Long> roomIds) {
        this.id = id;
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.stayCreatorId = stayCreatorId;
        this.paymentType = paymentType;
        this.roomIds = roomIds;
    }
}