package com.fit.vut.pis_hotel.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.save(user);


    }

    public void deleteStudent(Long id) {
        boolean exists = userRepository.existsById(id);
        if (exists) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalStateException("User does not exist.");
        }

    }

    @Transactional
    public void updateUser(Long id, String firstName, String lastName, String email,
                           String phoneNumber, String idNumber, String address) {
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
        if (isStringValid(idNumber, user.getIdNumber())) {
            user.setIdNumber(idNumber);
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
