package co.tz.settlo.api.controllers.shift_log;

import co.tz.settlo.api.controllers.shift.Shift;
import co.tz.settlo.api.controllers.shift.ShiftRepository;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ShiftLogService {

    private final ShiftLogRepository shiftLogRepository;
    private final ShiftRepository shiftRepository;
    private final StaffRepository staffRepository;

    public ShiftLogService(final ShiftLogRepository shiftLogRepository,
            final ShiftRepository shiftRepository, final StaffRepository staffRepository) {
        this.shiftLogRepository = shiftLogRepository;
        this.shiftRepository = shiftRepository;
        this.staffRepository = staffRepository;
    }

    public List<ShiftLogDTO> findAll() {
        final List<ShiftLog> shiftLogs = shiftLogRepository.findAll(Sort.by("id"));
        return shiftLogs.stream()
                .map(shiftLog -> mapToDTO(shiftLog, new ShiftLogDTO()))
                .toList();
    }

    public ShiftLogDTO get(final UUID id) {
        return shiftLogRepository.findById(id)
                .map(shiftLog -> mapToDTO(shiftLog, new ShiftLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ShiftLogDTO shiftLogDTO) {
        final ShiftLog shiftLog = new ShiftLog();
        mapToEntity(shiftLogDTO, shiftLog);
        return shiftLogRepository.save(shiftLog).getId();
    }

    public void update(final UUID id, final ShiftLogDTO shiftLogDTO) {
        final ShiftLog shiftLog = shiftLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shiftLogDTO, shiftLog);
        shiftLogRepository.save(shiftLog);
    }

    public void delete(final UUID id) {
        shiftLogRepository.deleteById(id);
    }

    private ShiftLogDTO mapToDTO(final ShiftLog shiftLog, final ShiftLogDTO shiftLogDTO) {
        shiftLogDTO.setId(shiftLog.getId());
        shiftLogDTO.setStartTime(shiftLog.getStartTime());
        shiftLogDTO.setEndTime(shiftLog.getEndTime());
        shiftLogDTO.setStatus(shiftLog.getStatus());
        shiftLogDTO.setIsArchived(shiftLog.getIsArchived());
        shiftLogDTO.setCanDelete(shiftLog.getCanDelete());
        shiftLogDTO.setShift(shiftLog.getShift() == null ? null : shiftLog.getShift().getId());
        shiftLogDTO.setStaff(shiftLog.getStaff() == null ? null : shiftLog.getStaff().getId());
        return shiftLogDTO;
    }

    private ShiftLog mapToEntity(final ShiftLogDTO shiftLogDTO, final ShiftLog shiftLog) {
        shiftLog.setStartTime(shiftLogDTO.getStartTime());
        shiftLog.setEndTime(shiftLogDTO.getEndTime());
        shiftLog.setStatus(shiftLogDTO.getStatus());
        shiftLog.setIsArchived(shiftLogDTO.getIsArchived());
        shiftLog.setCanDelete(shiftLogDTO.getCanDelete());
        final Shift shift = shiftLogDTO.getShift() == null ? null : shiftRepository.findById(shiftLogDTO.getShift())
                .orElseThrow(() -> new NotFoundException("shift not found"));
        shiftLog.setShift(shift);
        final Staff staff = shiftLogDTO.getStaff() == null ? null : staffRepository.findById(shiftLogDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        shiftLog.setStaff(staff);
        return shiftLog;
    }

}
