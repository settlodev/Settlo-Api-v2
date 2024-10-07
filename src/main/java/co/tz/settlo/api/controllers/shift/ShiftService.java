package co.tz.settlo.api.controllers.shift;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.department.DepartmentRepository;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.location.LocationRepository;
import co.tz.settlo.api.controllers.shift_log.ShiftLog;
import co.tz.settlo.api.controllers.shift_log.ShiftLogRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final ShiftLogRepository shiftLogRepository;

    public ShiftService(final ShiftRepository shiftRepository,
            final BusinessRepository businessRepository,
            final LocationRepository locationRepository,
            final DepartmentRepository departmentRepository,
            final ShiftLogRepository shiftLogRepository) {
        this.shiftRepository = shiftRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.shiftLogRepository = shiftLogRepository;
    }

    public List<ShiftDTO> findAll() {
        final List<Shift> shifts = shiftRepository.findAll(Sort.by("id"));
        return shifts.stream()
                .map(shift -> mapToDTO(shift, new ShiftDTO()))
                .toList();
    }

    public ShiftDTO get(final UUID id) {
        return shiftRepository.findById(id)
                .map(shift -> mapToDTO(shift, new ShiftDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ShiftDTO shiftDTO) {
        final Shift shift = new Shift();
        mapToEntity(shiftDTO, shift);
        return shiftRepository.save(shift).getId();
    }

    public void update(final UUID id, final ShiftDTO shiftDTO) {
        final Shift shift = shiftRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shiftDTO, shift);
        shiftRepository.save(shift);
    }

    public void delete(final UUID id) {
        shiftRepository.deleteById(id);
    }

    private ShiftDTO mapToDTO(final Shift shift, final ShiftDTO shiftDTO) {
        shiftDTO.setId(shift.getId());
        shiftDTO.setName(shift.getName());
        shiftDTO.setStartTime(shift.getStartTime());
        shiftDTO.setEndTime(shift.getEndTime());
        shiftDTO.setStatus(shift.getStatus());
        shiftDTO.setCanDelete(shift.getCanDelete());
        shiftDTO.setIsArchived(shift.getIsArchived());
        shiftDTO.setBusiness(shift.getBusiness() == null ? null : shift.getBusiness().getId());
        shiftDTO.setLocation(shift.getLocation() == null ? null : shift.getLocation().getId());
        shiftDTO.setDepartment(shift.getDepartment() == null ? null : shift.getDepartment().getId());
        return shiftDTO;
    }

    private Shift mapToEntity(final ShiftDTO shiftDTO, final Shift shift) {
        shift.setName(shiftDTO.getName());
        shift.setStartTime(shiftDTO.getStartTime());
        shift.setEndTime(shiftDTO.getEndTime());
        shift.setStatus(shiftDTO.getStatus());
        shift.setCanDelete(shiftDTO.getCanDelete());
        shift.setIsArchived(shiftDTO.getIsArchived());
        final Business business = shiftDTO.getBusiness() == null ? null : businessRepository.findById(shiftDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        shift.setBusiness(business);
        final Location location = shiftDTO.getLocation() == null ? null : locationRepository.findById(shiftDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        shift.setLocation(location);
        final Department department = shiftDTO.getDepartment() == null ? null : departmentRepository.findById(shiftDTO.getDepartment())
                .orElseThrow(() -> new NotFoundException("department not found"));
        shift.setDepartment(department);
        return shift;
    }

    public boolean nameExists(final String name) {
        return shiftRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Shift shift = shiftRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ShiftLog shiftShiftLog = shiftLogRepository.findFirstByShift(shift);
        if (shiftShiftLog != null) {
            referencedWarning.setKey("shift.shiftLog.shift.referenced");
            referencedWarning.addParam(shiftShiftLog.getId());
            return referencedWarning;
        }
        return null;
    }

}
