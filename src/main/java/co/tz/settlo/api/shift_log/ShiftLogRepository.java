package co.tz.settlo.api.shift_log;

import co.tz.settlo.api.shift.Shift;
import co.tz.settlo.api.staff.Staff;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShiftLogRepository extends JpaRepository<ShiftLog, UUID> {

    ShiftLog findFirstByShift(Shift shift);

    ShiftLog findFirstByStaff(Staff staff);

}
