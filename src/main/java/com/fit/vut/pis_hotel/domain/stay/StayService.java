package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.host.HostRepository;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDTO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceRepository;
import com.fit.vut.pis_hotel.domain.stay.service.enums.ServiceTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final HostRepository hostRepository;
    private final ServiceRepository serviceRepository;

    public List<StayDO> getStays() {
        return stayRepository.findAll();
    }

    public StayDO getStay(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + " does not exist."));
    }

    public List<StayDTO> getSimpleStays() {
        List<StayDO> stays = getStays();
        return stays.stream().map(StayDO::toDto).collect(Collectors.toList());
    }

    public StayDTO getSimpleStay(Long id) {
        return getStay(id).toDto();
    }

    public List<StayDO> getStaysByCreatorId(Long id) {
        List<StayDO> stays = getStays();
        return stays.stream().filter(s -> Objects.equals(s.getStayCreator().getId(), id)).collect(Collectors.toList());
    }

    public void createStayWithCreatorId(StayDTO stayDTO) {
        HostDO creator = hostRepository.findById(stayDTO.getStayCreatorId())
                .orElseThrow(() -> new IllegalStateException("User with id: " + stayDTO.getStayCreatorId() + " does not exist."));
        StayDO newStay = new StayDO(stayDTO.getAccommodatedNumber(), stayDTO.getDateFrom(), stayDTO.getDateTo(), stayDTO.getState(), stayDTO.getBoardType(), creator, stayDTO.getPaymentType(), List.of());
        createStay(newStay);
    }

    public void createStay(StayDO stay) {
        stayRepository.save(stay);
    }

    public void deleteStay(Long id) {
        boolean exists = stayRepository.existsById(id);
        if (exists) {
            List<ServiceDO> services = getStayServices(id);
            // removes all services which belongs to stay
            for (ServiceDO service : services) {
                deleteService(service.getId());
            }
            stayRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Stay with id: " + id + " does not exist.");
        }
    }

    @Transactional
    public void updateStay(Long id, Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StateEnum state, BoardTypeEnum boardType, PaymentTypeEnum paymentType) {

        StayDO stay = stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + " does not exist."));

        if (isIntegerValid(accommodatedNumber, stay.getAccommodatedNumber())) {
            stay.setAccommodatedNumber(accommodatedNumber);
        }

        if (dateFrom != null && dateFrom != stay.getDateFrom()) {
            stay.setDateFrom(dateFrom);
        }

        if (dateTo != null && dateTo != stay.getDateTo()) {
            stay.setDateTo(dateTo);
        }

        if (state != null && state != stay.getState()) {
            stay.setState(state);
        }

        if (boardType != null && boardType != stay.getBoardType()) {
            stay.setBoardType(boardType);
        }

        if (paymentType != null && paymentType != stay.getPaymentType()) {
            stay.setPaymentType(paymentType);
        }
    }

    public List<ServiceDO> getServices() {
        return serviceRepository.findAll();
    }

    public ServiceDO getService(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Service with id: " + id + " does not exist."));
    }

    public ServiceDO createAndAssignService(ServiceDTO service) {
        return createService(toServiceDo(service));
    }

    public ServiceDTO getSimpleService(Long id) {
        return getService(id).toDto();
    }

    public List<ServiceDTO> getSimpleServices() {
        return getServices().stream().map(ServiceDO::toDto).collect(Collectors.toList());
    }

    public List<ServiceDO> getStayServices(Long stayId) {
        return serviceRepository.findServicesByStayId(stayId);
    }

    public ServiceDO createService(ServiceDO service) {
        StayDO stay = service.getStay();
        LocalDateTime stayTo = stay.getDateTo().atTime(11, 0);
        LocalDateTime stayFrom = stay.getDateFrom().atTime(14, 0);
        LocalDateTime serviceTo = service.getTimeTo();
        LocalDateTime serviceFrom = service.getTimeFrom();

        if (serviceFrom.isAfter(stayFrom) &&
                serviceTo.isBefore(stayTo)) {
            serviceRepository.save(service);
            return service;
        } else {
            throw new IllegalStateException("Service can be created only during the stay with id " + stay.getId() + " (" + stayFrom + " - " + stayTo + "). You have provided service for: (" + serviceFrom + " - " + serviceTo + ").");
        }
    }

    public void deleteService(Long id) {
        boolean exists = serviceRepository.existsById(id);
        if (exists) {
            serviceRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Service with id: " + id + " does not exist.");
        }
    }

    public void updateService(Long id, LocalDateTime timeFrom, LocalDateTime timeTo, ServiceTypeEnum serviceType, PaymentTypeEnum paymentType, Integer bowlingTracks, StateEnum state) {
        ServiceDO service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Service with id: " + id + " does not exist."));

        if (timeFrom != null && timeFrom != service.getTimeFrom()) {
            service.setTimeFrom(timeFrom);
        }

        if (timeTo != null && timeTo != service.getTimeTo()) {
            service.setTimeTo(timeTo);
        }

        if (serviceType != null && serviceType != service.getServiceType()) {
            service.setServiceType(serviceType);
        }

        if (paymentType != null && paymentType != service.getPaymentType()) {
            service.setPaymentType(paymentType);
        }

        if (isIntegerValid(bowlingTracks, service.getBowlingTracks())) {
            service.setBowlingTracks(bowlingTracks);
        }

        if (state != null && state != service.getState()) {
            service.setState(state);
        }

    }

    public ServiceDO toServiceDo(ServiceDTO service) {
        return new ServiceDO(service.getTimeFrom(), service.getTimeTo(), service.getServiceType(), service.getPaymentType(), service.getBowlingTracks(), service.getState(), getStay(service.getStayId()));
    }

    private boolean isIntegerValid(Integer integerToValidate, Integer originalInteger) {
        return integerToValidate != null && integerToValidate > 0 && !Objects.equals(integerToValidate, originalInteger);
    }
}
