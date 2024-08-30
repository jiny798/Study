package security.demo.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.demo.admin.repository.RoleRepository;
import security.demo.admin.service.RoleService;
import security.demo.domain.entity.Role;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role getRole(long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    @Transactional
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public List<Role> getRolesWithoutExpression() {
        return roleRepository.findAllRolesWithoutExpression();
    }

    @Transactional
    public void createRole(Role role){
        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
