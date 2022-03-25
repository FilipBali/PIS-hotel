package com.fit.vut.pis_hotel.domain.stay.service;

import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.enums.ServiceTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
public class ServiceDO {

    @Id
    @SequenceGenerator(
            name = "service_sequence",
            sequenceName = "service_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "service_sequence"
    )
    private Long id;

    private LocalDateTime timeFrom;

    private LocalDateTime timeTo;

    private ServiceTypeEnum serviceType;

    private PaymentTypeEnum paymentType;

    private Integer bowlingTracks;

    private StateEnum state;

    @ManyToOne
    @JoinColumn(name = "FK_Stay", referencedColumnName = "id")
    private StayDO stay;

    public ServiceDO(LocalDateTime timeFrom, LocalDateTime timeTo, ServiceTypeEnum serviceType, PaymentTypeEnum paymentType, Integer bowlingTracks, StateEnum state, StayDO stay) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.serviceType = serviceType;
        this.paymentType = paymentType;
        this.bowlingTracks = bowlingTracks;
        this.state = state;
        this.stay = stay;
    }

    public ServiceDTO toDto() {
        return new ServiceDTO(id, timeFrom, timeTo, serviceType, paymentType, bowlingTracks, state, stay.getId());
    }

}

