package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDO getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void createUser(@RequestBody UserDO user) {
        userService.createUser(user);
    }

    @DeleteMapping(path = "{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteStudent(id);
    }

    @PutMapping(path = {"{id}"})
    public void updateUser(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) String address) {
        userService.updateUser(id, firstName, lastName, email, phoneNumber, idNumber, address);
    }


}
