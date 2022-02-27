package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;

import java.time.LocalDate;


public class StayDTO {

    private Long id;
    private Integer accommodatedNumber;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private StayStateEnum state;
    private BoardTypeEnum boardType;
    private Long stayCreatorId;
    private PaymentTypeEnum paymentType;

    public StayDTO(Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StayStateEnum state, BoardTypeEnum boardType, Long stayCreatorId, PaymentTypeEnum paymentType) {
        this.accommodatedNumber = accommodatedNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.state = state;
        this.boardType = boardType;
        this.stayCreatorId = stayCreatorId;
        this.paymentType = paymentType;
    }


    public Long getId() {
        return id;
    }

    public Integer getAccommodatedNumber() {
        return accommodatedNumber;
    }

    public void setAccommodatedNumber(Integer accommodatedNumber) {
        this.accommodatedNumber = accommodatedNumber;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public StayStateEnum getState() {
        return state;
    }

    public void setState(StayStateEnum state) {
        this.state = state;
    }

    public BoardTypeEnum getBoardType() {
        return boardType;
    }

    public void setBoardType(BoardTypeEnum boardType) {
        this.boardType = boardType;
    }

    public Long getStayCreatorId() {
        return stayCreatorId;
    }

    public void setStayCreatorId(Long stayCreatorId) {
        this.stayCreatorId = stayCreatorId;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }
}