package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.role.RoleDO;
import com.fit.vut.pis_hotel.domain.role.RoleService;
import com.fit.vut.pis_hotel.domain.role.RoleToUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public RoleDO createRole(@RequestBody RoleDO role) {
        return roleService.saveRole(role);
    }

    @PostMapping("/addtouser")
    public void addRoleToUser(@RequestBody RoleToUserDTO roleToUserDTO) {
        roleService.addRoleToUser(roleToUserDTO.getUserId(), roleToUserDTO.getRoleName());
    }

}
