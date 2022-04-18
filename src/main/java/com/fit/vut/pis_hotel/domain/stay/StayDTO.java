package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StayDTO {

    private Long id;
    private Integer accommodatedNumber;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private StateEnum state;
    private BoardTypeEnum boardType;
    private Integer boardPrice;
    private Long stayCreatorId;
    private PaymentTypeEnum paymentType;
    private List<Long> roomIds;
    private List<Long> hostsIds;

    public StayDTO(Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StateEnum state, BoardTypeEnum boardType, Long stayCreatorId, PaymentTypeEnum paymentType, List<Long> roomIds, List<Long> hostsIds) {
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.boardPrice = boardType.getPrice();
        this.stayCreatorId = stayCreatorId;
        this.paymentType = paymentType;
        this.roomIds = roomIds;
        this.hostsIds = hostsIds;
    }

    public StayDTO(Long id, Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StateEnum state, BoardTypeEnum boardType, Long stayCreatorId, PaymentTypeEnum paymentType, List<Long> roomIds, List<Long> hostsIds) {
        this.id = id;
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.boardPrice = boardType.getPrice();
        this.stayCreatorId = stayCreatorId;
        this.paymentType = paymentType;
        this.roomIds = roomIds;
        this.hostsIds = hostsIds;
    }
}