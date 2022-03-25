package com.fit.vut.pis_hotel.domain.host;

import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void updateHost(Long id, String firstName, String lastName, String email,
                           String phoneNumber, String idNumber, String address) {
        HostDO host = hostRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Host with id: " + id + "does not exist."));

        if (isStringValid(firstName, host.getFirstName())) {
            host.setFirstName(firstName);
        }
        if (isStringValid(lastName, host.getLastName())) {
            host.setLastName(lastName);
        }
        if (isStringValid(email, host.getEmail())) {
            if (isEmailTaken(email)) {
                throw new IllegalStateException("email taken");
            }
            host.setEmail(email);
        }
        if (isStringValid(phoneNumber, host.getPhoneNumber())) {
            host.setPhoneNumber(phoneNumber);
        }
        if (isStringValid(idNumber, host.getIdNumber())) {
            host.setIdNumber(idNumber);
        }
        if (isStringValid(address, host.getAddress())) {
            host.setAddress(address);
        }

    }

    private boolean isStringValid(String stringToValidate, String originalString) {
        return stringToValidate != null && stringToValidate.length() > 0 && !Objects.equals(originalString, stringToValidate);
    }

    private boolean isEmailTaken(String email) {
        Optional<HostDO> hostByEmail = hostRepository.findHostByEmail(email);
        return hostByEmail.isPresent();
    }


}
