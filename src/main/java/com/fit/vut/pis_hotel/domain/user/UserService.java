package com.fit.vut.pis_hotel.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .orElseThrow(() -> new IllegalStateException("User with email: " + email + "does not exist."));
        log.info("User found in DB.");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(roleDO -> authorities.add(new SimpleGrantedAuthority(roleDO.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public UserDO getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email: " + email + "does not exist."));
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
        log.info("Creating user: " + user.getFirstName() + " " + user.getLastName() + "with id: " + user.getId());
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
    public void updateUser(Long id, String firstName, String lastName, String email,
                           String phoneNumber, String address) {
        UserDO user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id: " + id + "does not exist."));

        if (isStringValid(firstName, user.getFirstName())) {
            user.setFirstName(firstName);
        }
        if (isStringValid(lastName, user.getLastName())) {
            user.setLastName(lastName);
        }
        if (isStringValid(email, user.getEmail())) {
            if (isEmailTaken(email)) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
        if (isStringValid(phoneNumber, user.getPhoneNumber())) {
            user.setPhoneNumber(phoneNumber);
        }

        if (isStringValid(address, user.getAddress())) {
            user.setAddress(address);
        }

    }

    private boolean isStringValid(String stringToValidate, String originalString) {
        return stringToValidate != null && stringToValidate.length() > 0 && !Objects.equals(originalString, stringToValidate);
    }

    private boolean isEmailTaken(String email) {
        Optional<UserDO> userByEmail = userRepository.findUserByEmail(email);
        return userByEmail.isPresent();
    }

}
