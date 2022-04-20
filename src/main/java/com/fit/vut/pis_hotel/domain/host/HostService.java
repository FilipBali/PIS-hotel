package com.fit.vut.pis_hotel.domain.host;

import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostService {

    private final HostRepository hostRepository;
    private final StayService stayService;

    public HostDO getHostByEmail(String email) {
        return hostRepository.findHostByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Host with email: " + email + "does not exist."));
    }

    public List<HostDO> getHosts() {
        return hostRepository.findAll();
    }

    public HostDO getHost(Long id) {
        return hostRepository.findById(id).orElse(null);
        //orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public void createHost(HostDO host) {
        if (isEmailTaken(host.getEmail())) {
            throw new IllegalStateException("email taken");
        }
        log.info("Creating host: " + host.getFirstName() + " " + host.getLastName() + "with id: " + host.getId());
        hostRepository.save(host);
    }

    public void deleteHost(Long id) {
        boolean exists = hostRepository.existsById(id);
        if (exists) {
            List<StayDO> stays = stayService.getStaysByCreatorId(id);
            // removes all stays where user is creator
            for (StayDO stay : stays) {
                stayService.deleteStay(stay.getId());
            }
            log.info("Deleting host with id: " + id);
            hostRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Host does not exist.");
        }

    }

    @Transactional
    public void updateHost(Long id, HostDO hostBody) {
        HostDO host = hostRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Host with id: " + id + "does not exist."));

        String firstName = hostBody.getFirstName();
        if (isStringValid(firstName, host.getFirstName())) {
            log.info("Changing firstName of host with id: " + host.getId() + " from: " + host.getFirstName() + " to: " + firstName + ".");
            host.setFirstName(firstName);
        }
        String lastName = hostBody.getLastName();
        if (isStringValid(lastName, host.getLastName())) {
            log.info("Changing lastName of host with id: " + host.getId() + " from: " + host.getLastName() + " to: " + lastName + ".");
            host.setLastName(lastName);
        }

        LocalDate birthDate = hostBody.getDateOfBirth();
        if (isDateValid(birthDate, host.getDateOfBirth())) {
            log.info("Changing date of birth of user with id: " + host.getId() + " from: " + host.getDateOfBirth() + " to: " + birthDate + ".");
            host.setDateOfBirth(birthDate);
        }

        String email = hostBody.getEmail();
        if (isStringValid(email, host.getEmail())) {
            if (isEmailTaken(email)) {
                throw new IllegalStateException("Email taken");
            }
            log.info("Changing email of host with id: " + host.getId() + " from: " + host.getEmail() + " to: " + email + ".");
            host.setEmail(email);
        }
        String phoneNumber = hostBody.getPhoneNumber();
        if (isStringValid(phoneNumber, host.getPhoneNumber())) {
            log.info("Changing phoneNumber of host with id: " + host.getId() + " from: " + host.getPhoneNumber() + " to: " + phoneNumber + ".");
            host.setPhoneNumber(phoneNumber);
        }

        String address = hostBody.getAddress();
        if (isStringValid(address, host.getAddress())) {
            log.info("Changing address of host with id: " + host.getId() + " from: " + host.getAddress() + " to: " + address + ".");
            host.setAddress(address);
        }

        String idNumber = hostBody.getIdNumber();
        if (isStringValid(idNumber, host.getIdNumber())) {
            log.info("Changing idNumber of host with id: " + host.getId() + " from: " + host.getIdNumber() + " to: " + idNumber + ".");
            host.setIdNumber(idNumber);
        }
    }

    private boolean isStringValid(String stringToValidate, String originalString) {
        return stringToValidate != null && stringToValidate.length() > 0 && !Objects.equals(originalString, stringToValidate);
    }

    private boolean isEmailTaken(String email) {
        Optional<HostDO> hostByEmail = hostRepository.findHostByEmail(email);
        return hostByEmail.isPresent();
    }

    private boolean isDateValid(LocalDate dateToValidate, LocalDate originaldate) {
        return dateToValidate != null && !Objects.equals(originaldate, dateToValidate);
    }


}
