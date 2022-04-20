package com.fit.vut.pis_hotel.domain.user;

import com.fit.vut.pis_hotel.domain.role.RoleDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDO user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email: " + email + " does not exist."));
        log.info("User found in DB.");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(roleDO -> authorities.add(new SimpleGrantedAuthority(roleDO.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public UserDO getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email: " + email + " does not exist."));
    }

    public List<UserDO> getUsers() {
        return userRepository.findAll();
    }

    public UserDO getUser(Long id) {
        return userRepository.findById(id).orElse(null);
        //orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public void createUser(UserDO user) {
        if (isEmailTaken(user.getEmail())) {
            throw new IllegalStateException("email taken");
        }
        log.info("Creating user: " + user.getFirstName() + " " + user.getLastName() + " with id: " + user.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (exists) {
            log.info("Deleting user with id: " + id);
            userRepository.deleteById(id);
        } else {
            throw new IllegalStateException("User does not exist.");
        }

    }

    @Transactional
    public void updateUser(Long id, UserDO userBody) {
        UserDO user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id: " + id + "does not exist."));

        String firstName = userBody.getFirstName();
        if (isStringValid(firstName, user.getFirstName())) {
            log.info("Changing firstName of user with id: " + user.getId() + " from: " + user.getFirstName() + " to: " + firstName + ".");
            user.setFirstName(firstName);
        }
        String lastName = userBody.getLastName();
        if (isStringValid(lastName, user.getLastName())) {
            log.info("Changing lastName of user with id: " + user.getId() + " from: " + user.getLastName() + " to: " + lastName + ".");
            user.setLastName(lastName);
        }
        String email = userBody.getEmail();
        if (isStringValid(email, user.getEmail())) {
            if (isEmailTaken(email)) {
                throw new IllegalStateException("Email taken");
            }
            log.info("Changing email of user with id: " + user.getId() + " from: " + user.getEmail() + " to: " + email + ".");
            user.setEmail(email);
        }

        LocalDate birthDate = userBody.getDateOfBirth();
        if (isDateValid(birthDate, user.getDateOfBirth())) {
            log.info("Changing date of birth of user with id: " + user.getId() + " from: " + user.getDateOfBirth() + " to: " + birthDate + ".");
            user.setDateOfBirth(birthDate);
        }

        String phoneNumber = userBody.getPhoneNumber();
        if (isStringValid(phoneNumber, user.getPhoneNumber())) {
            log.info("Changing phoneNumber of user with id: " + user.getId() + " from: " + user.getPhoneNumber() + " to: " + phoneNumber + ".");
            user.setPhoneNumber(phoneNumber);
        }

        String address = userBody.getAddress();
        if (isStringValid(address, user.getAddress())) {
            log.info("Changing address of user with id: " + user.getId() + " from: " + user.getAddress() + " to: " + address + ".");
            user.setAddress(address);
        }

        String password = userBody.getPassword();
        if (isStringValid(password, user.getPassword())) {
            log.info("Changing password of user with id: " + user.getId() + ".");
            user.setPassword(passwordEncoder.encode(password));
        }

        Collection<RoleDO> roles = userBody.getRoles();
        if (roles != null && !roles.isEmpty() && !roles.equals(user.getRoles())) {
            user.setRoles(roles);
        }

    }

    private boolean isStringValid(String stringToValidate, String originalString) {
        return stringToValidate != null && stringToValidate.length() > 0 && !Objects.equals(originalString, stringToValidate);
    }

    private boolean isEmailTaken(String email) {
        Optional<UserDO> userByEmail = userRepository.findUserByEmail(email);
        return userByEmail.isPresent();
    }

    private boolean isDateValid(LocalDate dateToValidate, LocalDate originaldate) {
        return dateToValidate != null && !Objects.equals(originaldate, dateToValidate);
    }

}
