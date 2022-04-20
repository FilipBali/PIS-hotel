package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.host.HostRepository;
import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.room.RoomRepository;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDTO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final HostRepository hostRepository;
    private final ServiceRepository serviceRepository;
    private final RoomRepository roomRepository;

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

    @Transactional
    public void createStayWithSimple(StayDTO stayDTO) {
        HostDO creator = hostRepository.findById(stayDTO.getStayCreatorId())
                .orElseThrow(() -> new IllegalStateException("Host with id: " + stayDTO.getStayCreatorId() + " does not exist."));
        List<RoomDO> rooms = roomRepository.findAllById(stayDTO.getRoomIds());
        StayDO newStay = new StayDO(stayDTO.getAccommodatedNumber(), stayDTO.getDateFrom(), stayDTO.getDateTo(), stayDTO.getState(), stayDTO.getBoardType(), creator, stayDTO.getPaymentType(), rooms);
        if (rooms.stream().allMatch(room -> isRoomAvailable(newStay, room))) {
            createStay(newStay);
        }
    }

    public void createStay(StayDO stay) {
        if (stay.getRooms().stream().allMatch(room -> isRoomAvailable(stay, room))) {
            stayRepository.save(stay);
        }
    }

    public void deleteStay(Long id) {
        boolean exists = stayRepository.existsById(id);
        if (exists) {
            List<ServiceDO> services = getStayServices(id);
            // removes all services which belong to stay
            for (ServiceDO service : services) {
                log.info("Deleting service with id: " + service.getId());
                deleteService(service.getId());
            }
            stayRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Stay with id: " + id + " does not exist.");
        }
    }

    @Transactional
    public void updateStay(Long id, StayDO stayBody) {

        StayDO stay = stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + " does not exist."));

        Integer accommodatedNumber = stayBody.getAccommodatedNumber();
        if (isIntegerValid(accommodatedNumber, stay.getAccommodatedNumber())) {
            stay.setAccommodatedNumber(accommodatedNumber);
        }

        LocalDate dateFrom = stayBody.getDateFrom();
        if (dateFrom != null && dateFrom != stay.getDateFrom()) {
            stay.setDateFrom(dateFrom);
        }

        LocalDate dateTo = stayBody.getDateTo();
        if (dateTo != null && dateTo != stay.getDateTo()) {
            stay.setDateTo(dateTo);
        }

        StateEnum state = stayBody.getState();
        if (state != null && state != stay.getState()) {
            stay.setState(state);
        }

        BoardTypeEnum boardType = stayBody.getBoardType();
        if (boardType != null && boardType != stay.getBoardType()) {
            stay.setBoardType(boardType);
        }

        PaymentTypeEnum paymentType = stayBody.getPaymentType();
        if (paymentType != null && paymentType != stay.getPaymentType()) {
            stay.setPaymentType(paymentType);
        }

        List<RoomDO> rooms = stayBody.getRooms();
        if (rooms != null && !rooms.isEmpty() && !rooms.equals(stay.getRooms())) {
            if (stay.getRooms().stream().allMatch(room -> isRoomAvailable(stay, room))) {
                stay.setRooms(rooms);
            }
        }

        List<HostDO> hosts = stayBody.getHosts();
        if (hosts != null && !hosts.isEmpty() && !hosts.equals(stay.getHosts())) {
            stay.setHosts(hosts);
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
        return isServiceDateValid(service) ? serviceRepository.save(service) : throwInvalidServiceDateException(service);
    }

    public void deleteService(Long id) {
        boolean exists = serviceRepository.existsById(id);
        if (exists) {
            serviceRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Service with id: " + id + " does not exist.");
        }
    }

    public void updateService(Long id, ServiceDO serviceBody) {
        ServiceDO service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Service with id: " + id + " does not exist."));

        if (serviceBody.getTimeFrom() != null && serviceBody.getTimeFrom() != service.getTimeFrom()) {
            if (isServiceDateValid(serviceBody, serviceBody.getTimeFrom(), null)) {
                service.setTimeFrom(serviceBody.getTimeFrom());
            }
        }

        if (serviceBody.getTimeTo() != null && serviceBody.getTimeTo() != service.getTimeTo()) {
            if (isServiceDateValid(serviceBody, null, serviceBody.getTimeTo())) {
                service.setTimeTo(serviceBody.getTimeTo());
            }
        }

        if (serviceBody.getServiceType() != null && serviceBody.getServiceType() != service.getServiceType()) {
            service.setServiceType(serviceBody.getServiceType());
        }

        if (serviceBody.getPaymentType() != null && serviceBody.getPaymentType() != service.getPaymentType()) {
            service.setPaymentType(serviceBody.getPaymentType());
        }

        if (isIntegerValid(serviceBody.getBowlingTracks(), service.getBowlingTracks())) {
            service.setBowlingTracks(serviceBody.getBowlingTracks());
        }

        if (serviceBody.getState() != null && serviceBody.getState() != service.getState()) {
            service.setState(serviceBody.getState());
        }

    }

    public ServiceDO toServiceDo(ServiceDTO service) {
        return new ServiceDO(service.getTimeFrom(), service.getTimeTo(), service.getServiceType(), service.getPaymentType(), service.getBowlingTracks(), service.getState(), getStay(service.getStayId()));
    }

    private boolean isIntegerValid(Integer integerToValidate, Integer originalInteger) {
        return integerToValidate != null && integerToValidate > 0 && !Objects.equals(integerToValidate, originalInteger);
    }

    private boolean isServiceDateValid(ServiceDO service) {
        StayDO stay = service.getStay();
        LocalDateTime stayTo = stay.getDateTo().atTime(11, 0);
        LocalDateTime stayFrom = stay.getDateFrom().atTime(14, 0);
        LocalDateTime serviceTo = service.getTimeTo();
        LocalDateTime serviceFrom = service.getTimeFrom();

        return serviceFrom.isAfter(stayFrom) && serviceTo.isBefore(stayTo);
    }

    private boolean isServiceDateValid(ServiceDO service, LocalDateTime newTimeFrom, LocalDateTime newTimeTo) {
        StayDO stay = service.getStay();
        LocalDateTime stayTo = stay.getDateTo().atTime(11, 0);
        LocalDateTime stayFrom = stay.getDateFrom().atTime(14, 0);
        LocalDateTime serviceTo = newTimeTo != null ? newTimeTo : service.getTimeTo();
        LocalDateTime serviceFrom = newTimeFrom != null ? newTimeFrom : service.getTimeFrom();

        return serviceFrom.isAfter(stayFrom) && serviceTo.isBefore(stayTo);
    }

    private ServiceDO throwInvalidServiceDateException(ServiceDO service) {
        throw new IllegalStateException("Service can be created only during the stay with id "
                + service.getStay().getId() + " ("
                + service.getStay().getDateFrom().atTime(14, 0) + " - "
                + service.getStay().getDateTo().atTime(11, 0) + "). You have provided service for: ("
                + service.getTimeFrom() + " - " + service.getTimeTo() + ").");
    }

    private boolean isRoomAvailable(StayDO stay, RoomDO room) {
        LocalDateTime dateFrom = stay.getDateFrom().atTime(14, 0);
        LocalDateTime dateTo = stay.getDateTo().atTime(11, 0);
        RoomDO realRoom = roomRepository.getById(room.getId());
        List<StayDO> stays = realRoom.getStays();
        for (StayDO oneStay : stays) {
            if (Objects.equals(oneStay.getId(), stay.getId())) {
                continue;
            }
            if (isDateInBetween(oneStay.getDateFrom().atTime(14, 0), oneStay.getDateTo().atTime(11, 0), dateFrom) ||
                    isDateInBetween(oneStay.getDateFrom().atTime(14, 0), oneStay.getDateTo().atTime(11, 0), dateTo)) {
                throw new IllegalStateException("Room with id: " + room.getId() +
                        " is already reserved for stay with id: " + oneStay.getId() +
                        " for date from: " + oneStay.getDateFrom().atTime(14, 0) +
                        " to: " + oneStay.getDateTo().atTime(11, 0) + ". ");
            }
        }
        return true;
    }

    private boolean isDateInBetween(final LocalDateTime min, final LocalDateTime max, final LocalDateTime date) {
        return !(date.isBefore(min) || date.isAfter(max));
    }
}
