package com.fit.vut.pis_hotel.domain.stay.service;

import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.enums.ServiceTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceDTO {

    private Long id;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private ServiceTypeEnum serviceType;
    private PaymentTypeEnum paymentType;
    private Integer bowlingTracks;
    private StateEnum state;
    private Long stayId;

    public ServiceDTO(LocalDateTime timeFrom, LocalDateTime timeTo, ServiceTypeEnum serviceType, PaymentTypeEnum paymentType, Integer bowlingTracks, StateEnum state, Long stayId) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.serviceType = serviceType;
        this.paymentType = paymentType;
        this.bowlingTracks = bowlingTracks;
        this.state = state;
        this.stayId = stayId;
    }

    public ServiceDTO(Long id, LocalDateTime timeFrom, LocalDateTime timeTo, ServiceTypeEnum serviceType, PaymentTypeEnum paymentType, Integer bowlingTracks, StateEnum state, Long stayId) {
        this.id = id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.serviceType = serviceType;
        this.paymentType = paymentType;
        this.bowlingTracks = bowlingTracks;
        this.state = state;
        this.stayId = stayId;
    }
}
