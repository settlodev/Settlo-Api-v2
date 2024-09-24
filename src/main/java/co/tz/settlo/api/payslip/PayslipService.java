package co.tz.settlo.api.payslip;

import co.tz.settlo.api.salary.Salary;
import co.tz.settlo.api.salary.SalaryRepository;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PayslipService {

    private final PayslipRepository payslipRepository;
    private final StaffRepository staffRepository;
    private final SalaryRepository salaryRepository;

    public PayslipService(final PayslipRepository payslipRepository,
            final StaffRepository staffRepository, final SalaryRepository salaryRepository) {
        this.payslipRepository = payslipRepository;
        this.staffRepository = staffRepository;
        this.salaryRepository = salaryRepository;
    }

    public List<PayslipDTO> findAll() {
        final List<Payslip> payslips = payslipRepository.findAll(Sort.by("id"));
        return payslips.stream()
                .map(payslip -> mapToDTO(payslip, new PayslipDTO()))
                .toList();
    }

    public PayslipDTO get(final UUID id) {
        return payslipRepository.findById(id)
                .map(payslip -> mapToDTO(payslip, new PayslipDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final PayslipDTO payslipDTO) {
        final Payslip payslip = new Payslip();
        mapToEntity(payslipDTO, payslip);
        return payslipRepository.save(payslip).getId();
    }

    public void update(final UUID id, final PayslipDTO payslipDTO) {
        final Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(payslipDTO, payslip);
        payslipRepository.save(payslip);
    }

    public void delete(final UUID id) {
        payslipRepository.deleteById(id);
    }

    private PayslipDTO mapToDTO(final Payslip payslip, final PayslipDTO payslipDTO) {
        payslipDTO.setId(payslip.getId());
        payslipDTO.setBaseSalary(payslip.getBaseSalary());
        payslipDTO.setNetSalary(payslip.getNetSalary());
        payslipDTO.setStartPeriod(payslip.getStartPeriod());
        payslipDTO.setEndPeriod(payslip.getEndPeriod());
        payslipDTO.setStatus(payslip.getStatus());
        payslipDTO.setCanDelete(payslip.getCanDelete());
        payslipDTO.setIsArchived(payslip.getIsArchived());
        payslipDTO.setStaff(payslip.getStaff() == null ? null : payslip.getStaff().getId());
        payslipDTO.setSalary(payslip.getSalary() == null ? null : payslip.getSalary().getId());
        return payslipDTO;
    }

    private Payslip mapToEntity(final PayslipDTO payslipDTO, final Payslip payslip) {
        payslip.setBaseSalary(payslipDTO.getBaseSalary());
        payslip.setNetSalary(payslipDTO.getNetSalary());
        payslip.setStartPeriod(payslipDTO.getStartPeriod());
        payslip.setEndPeriod(payslipDTO.getEndPeriod());
        payslip.setStatus(payslipDTO.getStatus());
        payslip.setCanDelete(payslipDTO.getCanDelete());
        payslip.setIsArchived(payslipDTO.getIsArchived());
        final Staff staff = payslipDTO.getStaff() == null ? null : staffRepository.findById(payslipDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        payslip.setStaff(staff);
        final Salary salary = payslipDTO.getSalary() == null ? null : salaryRepository.findById(payslipDTO.getSalary())
                .orElseThrow(() -> new NotFoundException("salary not found"));
        payslip.setSalary(salary);
        return payslip;
    }

}
