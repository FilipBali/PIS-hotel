package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final UserRepository userRepository;


    public List<StayDO> getStays() {
        return stayRepository.findAll();
    }

    public StayDO getStay(Long id) {
        return stayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stay with id: " + id + "does not exist."));
    }

    public void createStayWithCreatorId(StayDTO stayDTO) {
        UserDO creator = userRepository.findById(stayDTO.getStayCreatorId())
                .orElseThrow(() -> new IllegalStateException("User with id: " + stayDTO.getStayCreatorId() + "does not exist."));
        StayDO newStay = new StayDO(stayDTO.getAccommodatedNumber(), stayDTO.getDateFrom(), stayDTO.getDateTo(), stayDTO.getState(), stayDTO.getBoardType(), creator, stayDTO.getPaymentType());
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
