package com.fit.vut.pis_hotel.domain.role;

import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    @Transactional
    public void addRoleToUser(Long id, String roleName) {
        UserDO user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id: " + id + " does not exist."));
        log.info("Adding role {} to user with id {}", roleName, id);
        RoleDO role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }


    public RoleDO getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Role with id: " + id + " does not exist."));
    }

    public List<RoleDO> getRoles() {
        return roleRepository.findAll();
    }

    public void deleteRole(Long id) {
        RoleDO role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Role with id: " + id + " does not exist."));
        log.info("Deleting role with id: " + id + "and name: " + role.getName());
        roleRepository.delete(role);
    }
}
