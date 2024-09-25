package co.tz.settlo.api.role;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final BusinessRepository businessRepository;
    private final StaffRepository staffRepository;

    public RoleService(final RoleRepository roleRepository,
            final BusinessRepository businessRepository, final StaffRepository staffRepository) {
        this.roleRepository = roleRepository;
        this.businessRepository = businessRepository;
        this.staffRepository = staffRepository;
    }

    public List<RoleDTO> findAll() {
        final List<Role> roles = roleRepository.findAll(Sort.by("id"));
        return roles.stream()
                .map(role -> mapToDTO(role, new RoleDTO()))
                .toList();
    }

    public RoleDTO get(final UUID id) {
        return roleRepository.findById(id)
                .map(role -> mapToDTO(role, new RoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RoleDTO roleDTO) {
        final Role role = new Role();
        mapToEntity(roleDTO, role);
        return roleRepository.save(role).getId();
    }

    public void update(final UUID id, final RoleDTO roleDTO) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roleDTO, role);
        roleRepository.save(role);
    }

    public UUID getDefaultRole() {
        return roleRepository.findByNameIgnoreCase("User")
                .map(Role::getId)
                .orElseGet(this::createDefaultRole);
    }

    private UUID createDefaultRole() {
        RoleDTO defaultRoleDTO = new RoleDTO();
        defaultRoleDTO.setName("User");

        return create(defaultRoleDTO);
    }

    public boolean nameExists(final String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }

    public void delete(final UUID id) {
        roleRepository.deleteById(id);
    }

    private RoleDTO mapToDTO(final Role role, final RoleDTO roleDTO) {
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setCanDelete(role.getCanDelete());
        roleDTO.setStatus(role.getStatus());
        roleDTO.setIsArchived(role.getIsArchived());
        roleDTO.setBusiness(role.getBusiness() == null ? null : role.getBusiness().getId());
        return roleDTO;
    }

    private Role mapToEntity(final RoleDTO roleDTO, final Role role) {
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setCanDelete(roleDTO.getCanDelete());
        role.setStatus(roleDTO.getStatus());
        role.setIsArchived(roleDTO.getIsArchived());
        final Business business = roleDTO.getBusiness() == null ? null : businessRepository.findById(roleDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        role.setBusiness(business);
        return role;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Staff roleStaff = staffRepository.findFirstByRole(role);
        if (roleStaff != null) {
            referencedWarning.setKey("role.staff.role.referenced");
            referencedWarning.addParam(roleStaff.getId());
            return referencedWarning;
        }
        return null;
    }

}
