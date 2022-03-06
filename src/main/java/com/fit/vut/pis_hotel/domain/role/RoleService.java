package com.fit.vut.pis_hotel.domain.role;

import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleDO saveRole(RoleDO role) {
        log.info("Saving new role {} to database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(RoleDO role) {
        addRoleToUser(role.getId(), role.getName());
    }

    @Transactional
    public void addRoleToUser(Long id, String roleName) {
        UserDO user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id: " + id + "does not exist."));
        log.info("Adding role {} to user with id {}", roleName, id);
        RoleDO role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }


}
