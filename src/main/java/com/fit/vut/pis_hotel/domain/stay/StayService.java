package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.host.HostRepository;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final HostRepository hostRepository;


    public List<StayDO> getStays() {
        return stayRepository.findAll();
    }

    public StayDO getStay(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + "does not exist."));
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
                .orElseThrow(() -> new IllegalStateException("User with id: " + stayDTO.getStayCreatorId() + "does not exist."));
        StayDO newStay = new StayDO(stayDTO.getAccommodatedNumber(), stayDTO.getDateFrom(), stayDTO.getDateTo(), stayDTO.getState(), stayDTO.getBoardType(), creator, stayDTO.getPaymentType(), List.of());
        createStay(newStay);
    }

    public void createStay(StayDO stay) {
        stayRepository.save(stay);
    }

    public void deleteStay(Long id) {
        boolean exists = stayRepository.existsById(id);
        if (exists) {
            stayRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Stay with id: " + id + " does not exist.");
        }

    }

    @Transactional
    public void updateStay(Long id, Integer accommodatedNumber, LocalDate dateFrom, LocalDate dateTo, StayStateEnum state, BoardTypeEnum boardType, PaymentTypeEnum paymentType) {

        StayDO stay = stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + "does not exist."));

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

    private boolean isIntegerValid(Integer integerToValidate, Integer originalInteger) {
        return integerToValidate != null && integerToValidate > 0 && !Objects.equals(integerToValidate, originalInteger);
    }
}
