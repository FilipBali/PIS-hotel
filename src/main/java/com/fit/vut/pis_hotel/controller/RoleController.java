package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.role.RoleDO;
import com.fit.vut.pis_hotel.domain.role.RoleService;
import com.fit.vut.pis_hotel.domain.role.RoleToUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/{id}")
    public RoleDO getRole(@PathVariable("id") Long id) {
        return roleService.getRole(id);
    }

    @GetMapping()
    public List<RoleDO> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/create")
    public RoleDO createRole(@RequestBody RoleDO role) {
        return roleService.saveRole(role);
    }

    @PostMapping("/addtouser")
    public void addRoleToUser(@RequestBody RoleToUserDTO roleToUserDTO) {
        roleService.addRoleToUser(roleToUserDTO.getUserId(), roleToUserDTO.getRoleName());
    }

    @PostMapping("/addtouser/{userId}/{role}")
    public void addRoleToUser(@PathVariable("userId") Long userId, @PathVariable("roleId") String role) {
        roleService.addRoleToUser(userId, role);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
    }

}
